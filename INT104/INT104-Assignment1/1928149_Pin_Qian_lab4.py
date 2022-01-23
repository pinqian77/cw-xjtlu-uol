import csv
import numpy as np

csv_file = open('binary_data.csv')
csv_reader = csv.reader(csv_file, delimiter = ',')
data = list(csv_reader)
data_mat = np.mat(data) # make it to a matrix

def getPriorProb(data, label):
    cnt = 0
    for row in data:
        if row[5] == label:
            cnt += 1
    p = cnt / len(data)
    return p

def getConProb(data, index, a_tar, label):
    cnt = 0
    l_cnt = 0
    for row in data:
        if row[5] == label:
            l_cnt += 1
            if row[index] == a_tar:
                cnt += 1
    p = cnt / l_cnt
    return p

# Q1
print('The shape of the matrix is', data_mat.shape)
# Q2
print('******Q2*****')
print('p(l=0) =', getPriorProb(data, '0'))
print('p(l=1) =', getPriorProb(data, '1'))

# Q3
print('*******************Q3*******************')
for idx in range(0, 5):
    print('p(a_%d = 0 | l = 0) =' %(idx),
           getConProb(data, idx, '0','0'))
print('---------------------------------------')
for idx in range(0, 5):
    print('p(a_%d = 1 | l = 0) =' %(idx),
           getConProb(data, idx, '1','0'))
print('---------------------------------------')
for idx in range(0, 5):
    print('p(a_%d = 0 | l = 1) =' %(idx),
           getConProb(data, idx, '0','1'))
print('---------------------------------------')
for idx in range(0, 5):
    print('p(a_%d = 1 | l = 1) =' %(idx),
           getConProb(data, idx, '1','1'))