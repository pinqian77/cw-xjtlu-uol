############################################################################
### Written by Gaojie Jin and updated by Xiaowei Huang, 2021
###
### For a 2-nd year undergraduate student competition on 
### the robustness of deep neural networks, where a student 
### needs to develop 
### 1. an attack algorithm, and 
### 2. an adversarial training algorithm
###
### The score is based on both algorithms. 
############################################################################


import numpy as np
import pandas as pd
import torch
import torch.nn as nn
import torch.nn.functional as F
from torch.utils.data import Dataset, DataLoader
import torch.optim as optim
import torchvision
from torchvision import transforms
from torch.autograd import Variable
import argparse
import time

# input id
id_ = 1000

# setup training parameters
parser = argparse.ArgumentParser(description='PyTorch MNIST Training')
parser.add_argument('--batch-size', type=int, default=256, metavar='N',
                    help='input batch size for training (default: 128)')
parser.add_argument('--test-batch-size', type=int, default=256, metavar='N',
                    help='input batch size for testing (default: 128)')
parser.add_argument('--epochs', type=int, default=120, metavar='N',
                    help='number of epochs to train')
parser.add_argument('--lr', type=float, default=0.01, metavar='LR',
                    help='learning rate')
parser.add_argument('--no-cuda', action='store_true', default=False,
                    help='disables CUDA training')
parser.add_argument('--seed', type=int, default=521, metavar='S',
                    help='random seed (default: 1)')


parser. add_argument ('--random', default=True, help='random initialization for PGD')
parser.add_argument('--epsilon', default =0.109, help='perturbation')
parser.add_argument('--num-steps', default =23, help='perturb number of steps , FGSM: 1, PGD -20: 20')
parser.add_argument('--step-size', default =0.33, help='perturb step size , FGSM: 0.031 , PGD -20: 0.003')

parser.add_argument('--num_restart', default = 30, help='number of random restart times')

args = parser.parse_args(args=[]) 

# judge cuda is available or not
use_cuda = not args.no_cuda and torch.cuda.is_available()
device = torch.device("cuda" if use_cuda else "cpu")
# print(device)
# device = torch.device("cpu")

torch.manual_seed(args.seed)
kwargs = {'num_workers': 1, 'pin_memory': True} if use_cuda else {}

############################################################################
################    don't change the below code    #####################
############################################################################
train_set = torchvision.datasets.FashionMNIST(root='data', train=True, download=True, transform=transforms.Compose([transforms.ToTensor()]))
train_loader = DataLoader(train_set, batch_size=args.batch_size, shuffle=True)

test_set = torchvision.datasets.FashionMNIST(root='data', train=False, download=True, transform=transforms.Compose([transforms.ToTensor()]))
test_loader = DataLoader(test_set, batch_size=args.batch_size, shuffle=True)

# define fully connected network
class Net(nn.Module):
    def __init__(self):
        super(Net, self).__init__()
        self.fc1 = nn.Linear(28*28, 128)
        self.fc2 = nn.Linear(128, 64)
        self.fc3 = nn.Linear(64, 32)
        self.fc4 = nn.Linear(32, 10)

    def forward(self, x):
        x = self.fc1(x)
        x = F.relu(x)
        x = self.fc2(x)
        x = F.relu(x)
        x = self.fc3(x)
        x = F.relu(x)
        x = self.fc4(x)
        output = F.log_softmax(x, dim=1)
        return output

##############################################################################
#############    end of "don't change the below code"   ######################
##############################################################################

def pgd_normal(model, X, y,
               epsilon=args.epsilon,
               num_steps =args.num_steps,
               step_size =args.step_size):
    X_pgd = Variable(X.data , requires_grad =True)
    if args.random:
        random_noise = torch.FloatTensor(*X_pgd.shape).uniform_(-epsilon ,epsilon).to(device)
        X_pgd = Variable(X_pgd.data + random_noise , requires_grad =True)
    for _ in range(num_steps):
        opt = optim.SGD([X_pgd], lr=1e-3)
        opt.zero_grad()
        with torch.enable_grad():
            loss = nn.CrossEntropyLoss()(model(X_pgd), y)
        loss.backward()
        eta = step_size * X_pgd.grad.data.sign()
        X_pgd = Variable(X_pgd.data + eta , requires_grad =True)
        eta = torch.clamp(X_pgd.data - X.data , -epsilon , epsilon)
        X_pgd = Variable(X.data + eta , requires_grad =True)
        X_pgd = Variable(torch.clamp(X_pgd , 0, 1.0) , requires_grad =True)
    return X_pgd

def pgd_restart(model, X, y,
                epsilon = args.epsilon,
                num_steps = args.num_steps,
                step_size = args.step_size,
                restarts = args.num_restart):
    max_loss = 0.
    res_X = Variable(X.data , requires_grad =True)
    
    for i in range(restarts):
        X_pgd = Variable(X.data , requires_grad =True)
        if args.random:
            random_noise = torch.FloatTensor(*X_pgd.shape).uniform_(-epsilon ,epsilon).to(device)
            random_noise = Variable(random_noise.data, requires_grad =True)
            X_pgd = Variable(X_pgd.data + random_noise , requires_grad =True)
        for _ in range(num_steps):
            opt = optim.SGD([X_pgd], lr=1e-3)
            opt.zero_grad()
            with torch.enable_grad():
                loss = nn.CrossEntropyLoss()(model(X_pgd), y)
            loss.backward()
            eta = step_size * X_pgd.grad.data.sign()
            X_pgd = Variable(X_pgd.data + eta , requires_grad =True)
            eta = torch.clamp(X_pgd.data - X.data , -epsilon , epsilon)
            X_pgd = Variable(X.data + eta , requires_grad =True)
            X_pgd = Variable(torch.clamp(X_pgd , 0, 1.0) , requires_grad =True)
        if (loss > max_loss):
            max_loss = loss
            res_X = X_pgd
    return res_X

def pgd_target(model, X, y,
               epsilon=args.epsilon,
               num_steps =args.num_steps,
               step_size =args.step_size,
               y_targ = 2):
    X_pgd = Variable(X.data , requires_grad =True)
    if args.random:
        random_noise = torch.FloatTensor(*X_pgd.shape).uniform_(-epsilon ,epsilon).to(device)
        X_pgd = Variable(X_pgd.data + random_noise , requires_grad =True)
    for _ in range(num_steps):
        opt = optim.SGD([X_pgd], lr=1e-3)
        opt.zero_grad()
        with torch.enable_grad():
            yp = model(X_pgd)
            loss = 2*yp[:,y_targ].sum() - yp.sum()
        loss.backward()
        eta = step_size * X_pgd.grad.data.sign()
        X_pgd = Variable(X_pgd.data + eta , requires_grad =True)
        eta = torch.clamp(X_pgd.data - X.data , -epsilon , epsilon)
        X_pgd = Variable(X.data + eta , requires_grad =True)
        X_pgd = Variable(torch.clamp(X_pgd , 0, 1.0) , requires_grad =True)
    return X_pgd

'generate adversarial data, you can define your adversarial method'
def adv_attack(model, X, y, device):
    X_adv = Variable(X.data)
    
    ################################################################################################
    ## Note: below is the place you need to edit to implement your own attack algorithm
    ################################################################################################
    # 1. Baseline
    # random_noise = torch.FloatTensor(*X_adv.shape).uniform_(-0.1, 0.1).to(device)
    # X_adv = Variable(X_adv.data + random_noise)

    # 2. pgd_normal
    X_adv = pgd_normal(model , X, y,
                       epsilon=args.epsilon,
                       num_steps =args.num_steps,
                       step_size =args.step_size)

    # 3. pgd_restart
    # X_adv = pgd_restart(model , X, y,
    #                 epsilon=args.epsilon,
    #                 num_steps =args.num_steps,
    #                 step_size =args. step_size,
    #                 restarts = args.num_restart)
    
    # 4. pgd_target
    # X_adv = pgd_target(model, X, y,
    #            epsilon=args.epsilon,
    #            num_steps =args.num_steps,
    #            step_size =args.step_size,
    #            y_targ = 2)

    # 5. torchattck_pgd
    # with torch.enable_grad():
    #     atk = torchattacks.PGD(model, 
    #                            eps=args.epsilon, 
    #                            alpha=args.step_size, 
    #                            steps=args.num_steps)
    #     X_adv = atk(X, y)

    # 6. torchattack_FAB
    # with torch.enable_grad():
    #     atk = torchattacks.FAB(model, 
    #                            eps=args.epsilon, 
    #                            steps=args.num_steps, 
    #                            n_classes=5, 
    #                            n_restarts=1, 
    #                            targeted=True)
    #     X_adv = atk(X, y)
    
    # 7. torchattack_APGDT
    # with torch.enable_grad():
    #     atk = torchattacks.FFGSM(model, 
    #                              eps=args.epsilon, 
    #                              alpha=args.step_size)
    #     X_adv = atk(X, y)

    # 8. torchattack_EOTPGD
    # with torch.enable_grad():
    #     atk = torchattacks.EOTPGD(model, 
    #                               eps=args.epsilon, 
    #                               alpha=args.step_size, 
    #                               steps=args.num_steps, 
    #                               eot_iter=2)
    #     X_adv = atk(X, y)

    # 8. torchattack_RFGSM
    # with torch.enable_grad():
    #     atk = torchattacks.RFGSM(model, 
    #                              eps=args.epsilon, 
    #                              alpha=args.step_size, 
    #                              steps=args.num_steps)
    #     X_adv = atk(X, y)

    ################################################################################################
    ## end of attack method
    ################################################################################################
    
    return X_adv

'train function, you can use adversarial training'
def train(args, model, device, train_loader, optimizer, epoch):
    model.train()
    for batch_idx, (data, target) in enumerate(train_loader):
        data, target = data.to(device), target.to(device)
        data = data.view(data.size(0),28*28)
        
        # normal loss
        optimizer.zero_grad()
        loss = F.nll_loss(model(data), target)
        loss.backward()
        optimizer.step()
        
        # adversarial loss
        # use adverserial data to train the defense model
        optimizer.zero_grad()
        adv_data = adv_attack(model, data, target, device=device)
        loss = F.nll_loss(model(adv_data), target)
        loss.backward()
        optimizer.step()

def eval_test(model, device, test_loader):
    model.eval()
    test_loss = 0
    correct = 0
    with torch.no_grad():
        for data, target in test_loader:
            data, target = data.to(device), target.to(device)
            data = data.view(data.size(0),28*28)
            output = model(data)
            test_loss += F.nll_loss(output, target, size_average=False).item()
            pred = output.max(1, keepdim=True)[1]
            correct += pred.eq(target.view_as(pred)).sum().item()
    test_loss /= len(test_loader.dataset)
    test_accuracy = correct / len(test_loader.dataset)
    return test_loss, test_accuracy

def eval_adv_test(model, device, test_loader):
    model.eval()
    test_loss = 0
    correct = 0
    with torch.no_grad():
        for data, target in test_loader:
            data, target = data.to(device), target.to(device)
            data = data.view(data.size(0),28*28)
            adv_data = adv_attack(model, data, target, device=device)
            output = model(adv_data)
            test_loss += F.nll_loss(output, target, size_average=False).item()
            pred = output.max(1, keepdim=True)[1]
            correct += pred.eq(target.view_as(pred)).sum().item()
    test_loss /= len(test_loader.dataset)
    test_accuracy = correct / len(test_loader.dataset)
    return test_loss, test_accuracy

'compute perturbation distance'
def p_distance(model, train_loader, device):
    p = []
    for batch_idx, (data, target) in enumerate(train_loader):
        data, target = data.to(device), target.to(device)
        data = data.view(data.size(0),28*28)
        adv_data = adv_attack(model, data, target, device=device)
        p.append(torch.norm(data-adv_data, float('inf')))
    print('epsilon p: ',max(p))

def train_model():
    model = Net().to(device)
    
    ################################################################################################
    ## Note: below is the place you need to edit to implement your own training algorithm
    ##       You can also edit the functions such as train(...). 
    ################################################################################################
    best_advacc = 0.0
    optimizer = optim.SGD(model.parameters(), lr=args.lr)
    for epoch in range(1, args.epochs + 1):
        start_time = time.time()
        
        #training
        train(args, model, device, train_loader, optimizer, epoch)
        
        #get trnloss and testloss
        trnloss, trnacc = eval_test(model, device, train_loader)
        advloss, advacc = eval_adv_test(model, device, train_loader)
        
        #print trnloss and testloss
        print('Epoch '+str(epoch)+': '+str(int(time.time()-start_time))+'s', end=', ')
        print('trn_loss: {:.4f}, trn_acc: {:.2f}%'.format(trnloss, 100. * trnacc), end=', ')
        print('adv_loss: {:.4f}, adv_acc: {:.2f}%'.format(advloss, 100. * advacc))

        #save model based on best advacc\
        if (advacc > best_advacc):
            best_advacc = advacc
            torch.save(model.state_dict(), str(id_)+'_' + str(epoch) + '.pt')
            adv_tstloss, adv_tstacc = eval_adv_test(model, device, test_loader)
            print('Your estimated attack ability, by applying your attack method on your own trained model, is: {:.4f}'.format(1/adv_tstacc))
            print('Your estimated defence ability, by evaluating your own defence model over your attack, is: {:.4f}'.format(adv_tstacc))
    ################################################################################################
    ## end of training method
    ################################################################################################
    return model

################################################################################################
## Note: below is for testing/debugging purpose, please comment them out in the submission file
################################################################################################
    
'Comment out the following command when you do not want to re-train the model'
'In that case, it will load a pre-trained model you saved in train_model()'
# model = train_model()

'Call adv_attack() method on a pre-trained model'
'the robustness of the model is evaluated against the infinite-norm distance measure'
'!!! important: MAKE SURE the infinite-norm distance (epsilon p) less than 0.11 !!!'

# attck on model without defense
# 0. baseline advacc = 0.7989
# 1. pgd_normal advacc = 0.1491
# 2. pgd_restart advacc = 0.1489 (restart = 30)
# 3. pgd_target advacc = 0.6472 

# defense 
# 1. e:50, b:128, lr:0.01, pgd_normal ---> pgd_normal: acc = 0.2998, advacc = 0.2937
#                                          pgd_restart:acc = 0.2998, advacc = 0.2928
# 2. e:50, b:128, lr:0.01, origin + pgd_normal ---> pgd_normal: acc = 0.8197, advacc = 0.6838
#                                                   pgd_restart:acc = 0.8197, advacc = 0.6868
# 3. e:50, b:256, lr:0.01, origin + pgd_normal ---> pgd_normal: acc = 0.8105, advacc = 0.6621
#                                                   pgd_restart:acc = 0.8105, advacc = 0.6617
# 4. e:100, b:128, lr:0.01, origin + pgd_normal ---> pgd_normal: acc = 0.8107, advacc = 0.6870
#                                                    pgd_restart:acc = 0.8107, advacc = 0.6854
# 5. e:100, b:256, lr:0.01, origin + pgd_normal, wd: 2e-4 ---> pgd_normal: acc = 0.8181, advacc = 0.6831
#
# 6. e:100, b:256, lr:0.01, origin + pgd_normal, loss:nll ---> pgd_normal: acc = 0.8159, advacc = 0.6831
#                                                              BIM: acc = 0.8159, advacc = 0.6936
#                                                              FBA_True: acc = 0.8159, advacc = 0.6798
#                                                              FFGSM: acc = 0.8159, advacc = 0.7110
#                                                              EOTPGD: acc = 0.8159, advacc = 0.6925
#                                                              RFGSM: acc = 0.8159, advacc = 0.6896
# model = Net().to(device)
# model.load_state_dict(torch.load('201600772.pt'))
# loss, acc = eval_test(model, device, test_loader)
# advloss, advacc = eval_adv_test(model, device, test_loader)
# print(acc, advacc)
# p_distance(model, test_loader, device)