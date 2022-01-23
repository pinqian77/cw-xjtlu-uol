import numpy as np
from nltk.stem.porter import *
import codecs
import os
import re

# read data
path = "dataset/"
file_dirs = os.listdir(path)
dataset = [] # N = len(dataset) = 2726
for file in file_dirs:
    file_path = path + file + '/' # e.g. /dataset/alt.atheism/
    txt_dirs = os.listdir(file_path)
    for txt in txt_dirs:
        txt_path = file_path + txt  # e.g. /dataset/alt.atheism/49960
        data = codecs.open(txt_path,'r',encoding='Latin1')
        dataset.append(data.read().split())

# read stopwords
f = open('stopwords.txt', 'r', encoding='Latin1')
stop_words = set(f.read().split())

# data preprocessing
stemmer = PorterStemmer()
for doc_p in range(len(dataset)):
    word_p = 0
    for _ in range(len(dataset[doc_p])):
        word = re.sub(r'[^a-z]', '', dataset[doc_p][word_p].lower()).strip() # convert to lower case
        if word not in stop_words and word.isalpha(): # remove stopwords and non-alphabet characters
            dataset[doc_p][word_p] = stemmer.stem(word) # perform word stemming
            word_p += 1
        else:
            del dataset[doc_p][word_p]

# unique words counting
words_pool = []
for data in dataset:
    words_pool += data # merge all words
unique_words = set(words_pool) # use set to get unique words

D = len(unique_words) # D = 27742
N = len(dataset) # N = 2726

# term frequency
tf = np.zeros(shape = (N, D)) # a numpy array storing term frequency
col = 0
for word in unique_words:     # for a word in unique_words
    row = 0                   # row starts from 0
    for doc in dataset:       # for each document in dataset
        tf[row, col] = doc.count(word)  # fill in with the count number of the word
        row += 1
    col += 1

# document frequency
df = tf
df[df > 0] = 1  # assign number which larger than 1 to 1
df = np.sum(df, axis = 0) # sum up colunm by colunm to get docement frequency

# result
A = tf * np.log(N / df) # mutiply TF and IDF
A /= np.linalg.norm(A, axis=1, keepdims=True) # normalize the representation row by row

# save
np.savez('train-20ng.npz', X=A)