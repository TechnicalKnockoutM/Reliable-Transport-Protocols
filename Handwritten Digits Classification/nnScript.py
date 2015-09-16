import numpy as np
from scipy.optimize import minimize
from scipy.io import loadmat
from scipy.stats import logistic
from math import sqrt
import time
import pickle

t0 = time.time()
def initializeWeights(n_in,n_out):
    """
    # initializeWeights return the random weights for Neural Network given the
    # number of node in the input layer and output layer

    # Input:
    # n_in: number of nodes of the input layer
    # n_out: number of nodes of the output layer
       
    # Output: 
    # W: matrix of random initial weights with size (n_out x (n_in + 1))"""
    
    epsilon = sqrt(6) / sqrt(n_in + n_out + 1);
    W = (np.random.rand(n_out, n_in + 1)*2* epsilon) - epsilon;
    return W
    
    
    
def sigmoid(z):
    
    """# Notice that z can be a scalar, a vector or a matrix
    # return the sigmoid of input z"""
    
    return  logistic.cdf(z)#your code here
    
    

def preprocess():
    """ Input:
     Although this function doesn't have any input, you are required to load
     the MNIST data set from file 'mnist_all.mat'.

     Output:
     train_data: matrix of training set. Each row of train_data contains 
       feature vector of a image
     train_label: vector of label corresponding to each image in the training
       set
     validation_data: matrix of training set. Each row of validation_data 
       contains feature vector of a image
     validation_label: vector of label corresponding to each image in the 
       training set
     test_data: matrix of training set. Each row of test_data contains 
       feature vector of a image
     test_label: vector of label corresponding to each image in the testing
       set

     Some suggestions for preprocessing step:
     - divide the original data set to training, validation and testing set
           with corresponding labels
     - convert original data set from integer to double by using double()
           function
     - normalize the data to [0, 1]
     - feature selection"""
    
    mat = loadmat('D:\Nikhil\Savefiles\Downloads\CSE 14\ML CSE574\PA1\mnist_all.mat') #loads the MAT object as a Dictionary
    
    #Pick a reasonable size for validation data
    

    #Your code here
    train_data = np.array([])
    train_label = np.zeros((50000,1))
    validation_data = np.array([])
    validation_label = np.zeros((10000,1))
    test_data = np.array([])
    test_label = np.zeros((10000,1))
    
    
    test0=mat.get('test0')
    test_temp_label=np.zeros(len(test0))
    test_temp_label.fill(0)
    test0=np.column_stack((test0,test_temp_label))
    
    test1=mat.get('test1')
    test_temp_label=np.zeros(len(test1))
    test_temp_label.fill(1)
    test1=np.column_stack((test1,test_temp_label))

    test2=mat.get('test2')
    test_temp_label=np.zeros(len(test2))
    test_temp_label.fill(2)
    test2=np.column_stack((test2,test_temp_label))

    test3=mat.get('test3')
    test_temp_label=np.zeros(len(test3))
    test_temp_label.fill(3)
    test3=np.column_stack((test3,test_temp_label))
    
    test4=mat.get('test4')
    test_temp_label=np.zeros(len(test4))
    test_temp_label.fill(4)
    test4=np.column_stack((test4,test_temp_label))

    test5=mat.get('test5')
    test_temp_label=np.zeros(len(test5))
    test_temp_label.fill(5)
    test5=np.column_stack((test5,test_temp_label))

    test6=mat.get('test6')
    test_temp_label=np.zeros(len(test6))
    test_temp_label.fill(6)
    test6=np.column_stack((test6,test_temp_label))

    test7=mat.get('test7')
    test_temp_label=np.zeros(len(test7))
    test_temp_label.fill(7)
    test7=np.column_stack((test7,test_temp_label))

    test8=mat.get('test8')
    test_temp_label=np.zeros(len(test8))
    test_temp_label.fill(8)
    test8=np.column_stack((test8,test_temp_label))
    
    test9=mat.get('test9')
    test_temp_label=np.zeros(len(test9))
    test_temp_label.fill(9)
    test9=np.column_stack((test9,test_temp_label))


    #print "test data"
    test_temp=np.array(np.vstack((test0,test1, test2, test3, test4, test5, test6, test7, test8, test9)))
    #print test_temp.shape
    np.random.shuffle(test_temp)
    test_label=test_temp[:,784]
    test_data=test_temp[:,0:784]
    #print test_label.shape
    #print test_data.shape
    np.divide(test_data,255.0,test_data)
    #print test_data.shape
    #print test_data.dtype
    

    train0=mat.get('train0')
    test_temp_label=np.zeros(len(train0))
    test_temp_label.fill(0)
    train0=np.column_stack((train0,test_temp_label))
    
    train1=mat.get('train1')
    test_temp_label=np.zeros(len(train1))
    test_temp_label.fill(1)
    train1=np.column_stack((train1,test_temp_label))
    
    train2=mat.get('train2')
    test_temp_label=np.zeros(len(train2))
    test_temp_label.fill(2)
    train2=np.column_stack((train2,test_temp_label))

    train3=mat.get('train3')
    test_temp_label=np.zeros(len(train3))
    test_temp_label.fill(3)
    train3=np.column_stack((train3,test_temp_label))

    train4=mat.get('train4')
    test_temp_label=np.zeros(len(train4))
    test_temp_label.fill(4)
    train4=np.column_stack((train4,test_temp_label))

    train5=mat.get('train5')
    test_temp_label=np.zeros(len(train5))
    test_temp_label.fill(5)
    train5=np.column_stack((train5,test_temp_label))
    
    train6=mat.get('train6')
    test_temp_label=np.zeros(len(train6))
    test_temp_label.fill(6)
    train6=np.column_stack((train6,test_temp_label))

    train7=mat.get('train7')
    test_temp_label=np.zeros(len(train7))
    test_temp_label.fill(7)
    train7=np.column_stack((train7,test_temp_label))
    
    train8=mat.get('train8')
    test_temp_label=np.zeros(len(train8))
    test_temp_label.fill(8)
    train8=np.column_stack((train8,test_temp_label))
    
    train9=mat.get('train9')
    test_temp_label=np.zeros(len(train9))
    test_temp_label.fill(9)
    train9=np.column_stack((train9,test_temp_label))

    train_temp=np.array(np.vstack((train0,train1, train2, train3, train4, train5, train6, train7, train8, train9)))
    #print "train_to be divided"
    #print train_temp.shape
    np.random.shuffle(train_temp)

    #print "train data"
    train_temp_f=train_temp[0:50000,:]
    #print train_temp_f.shape
    
    train_label=train_temp_f[:,784]
    #print train_label.shape
    train_data=train_temp_f[:,0:784]
    #print train_data.shape
    np.divide(train_data,255.0,train_data)
    #print train_data.shape
    #print train_data.dtype

    #print "validation data"
    valid_temp=train_temp[50000:60000,:]
    #print valid_temp.shape

    validation_label=valid_temp[:,784]
    #print validation_label.shape
    validation_data=valid_temp[:,0:784]
    #print validation_data.shape
    np.divide(validation_data,255.0,validation_data)
    #print validation_data.shape
    #print validation_data.dtype
    
   # print ("training data shape pre")
    #print train_data.shape
    
    #Feature Reduction start
    all_data=np.array(np.vstack((train_data, validation_data, test_data)))
    N = np.all(all_data == all_data[0,:], axis = 0)
    all_data = all_data[:,~N]
    
    train_data = all_data[0:len(train_data),:]
    validation_data = all_data[len(train_data): (len(train_data) + len(validation_data)),:]
    test_data = all_data[(len(train_data) + len(validation_data)): (len(train_data) + len(validation_data) + len(test_data)),:]
    #Feature Reducton end
    
    #print train_label.shape
    
    return train_data, train_label, validation_data, validation_label, test_data, test_label
    
    
    

def nnObjFunction(params, *args):
    """% nnObjFunction computes the value of objective function (negative log 
    %   likelihood error function with regularization) given the parameters 
    %   of Neural Networks, thetraining data, their corresponding training 
    %   labels and lambda - regularization hyper-parameter.

    % Input:
    % params: vector of weights of 2 matrices w1 (weights of connections from
    %     input layer to hidden layer) and w2 (weights of connections from
    %     hidden layer to output layer) where all of the weights are contained
    %     in a single vector.
    % n_input: number of node in input layer (not include the bias node)
    % n_hidden: number of node in hidden layer (not include the bias node)
    % n_class: number of node in output layer (number of classes in
    %     classification problem
    % training_data: matrix of training data. Each row of this matrix
    %     represents the feature vector of a particular image
    % training_label: the vector of truth label of training images. Each entry
    %     in the vector represents the truth label of its corresponding image.
    % lambda: regularization hyper-parameter. This value is used for fixing the
    %     overfitting problem.
       
    % Output: 
    % obj_val: a scalar value representing value of error function
    % obj_grad: a SINGLE vector of gradient value of error function
    % NOTE: how to compute obj_grad
    % Use backpropagation algorithm to compute the gradient of error function
    % for each weights in weight matrices.

    %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    % reshape 'params' vector into 2 matrices of weight w1 and w2
    % w1: matrix of weights of connections from input layer to hidden layers.
    %     w1(i, j) represents the weight of connection from unit j in input 
    %     layer to unit i in hidden layer.
    % w2: matrix of weights of connections from hidden layer to output layers.
    %     w2(i, j) represents the weight of connection from unit j in hidden 
    %     layer to unit i in output layer."""
    
    n_input, n_hidden, n_class, training_data, training_label, lambdaval = args
    
    w1 = params[0:n_hidden * (n_input + 1)].reshape( (n_hidden, (n_input + 1)))
    #print w1.shape
    w2 = params[(n_hidden * (n_input + 1)):].reshape((n_class, (n_hidden + 1)))
    #print w2.shape
    obj_val = 0  
     
    #Your code here
    #
    #
    #
    #
    #Feed Forward Starts
    training_data_bias_node=np.zeros(len(training_data))
    training_data_bias_node.fill(1)    
    training_data=np.column_stack([training_data,training_data_bias_node]) 
    #print training_data.shape  
    w1_t=np.transpose(w1)
    aj=np.dot(training_data,w1_t)
    #print aj.shape   
    zj=sigmoid(aj) 
   
    zj_bias_node=np.zeros(len(training_data))
    zj_bias_node.fill(1)    
    zj=np.column_stack([zj,zj_bias_node])
    #print zj.shape
    w2_t=np.transpose(w2)      
    bl=np.dot(zj,w2_t)
    ol=sigmoid(bl)
    #print ol.shape
   
    #Feed Forward Ends

    
    #Training Label modification (yl)
    
    #print training_label.shape
    yl=np.zeros((len(training_data),10))
    for x in range (0,len(training_data)):
        yl[x][training_label[x]]=1
    #print yl.shape       
      
    #Training Label modification ends
    
    #Gradiance Start
    
    delta_l=ol-yl
    delta_l_transpose=np.transpose(delta_l)    
    grad_w2=np.dot(delta_l_transpose,zj)
    #print grad_w2.shape    
 
    grad_w1_param3=np.dot(delta_l,w2)
    #grad_w1_param3=grad_w1_param3[:,0:n_hidden]
    #print grad_w1_param3.shape
    grad_w1_param1= (1 - zj) * zj
    #grad_w1_param1=grad_w1_param1[:,0:n_hidden]
    #print grad_w1_param1.shape
    grad_w1_param2=grad_w1_param1 * grad_w1_param3
    grad_w1_param2_transpose=np.transpose(grad_w1_param2)
    #print grad_w1_param2.shape
    grad_w1=np.dot(grad_w1_param2_transpose,training_data)
    grad_w1=grad_w1[0:n_hidden,:]
    #print grad_w1.shape
    #what is xp 
    #Gradiance Ends
    
    #Error Function starts    
    log_ol=np.log(ol)
    error_function_param1=np.multiply(yl,log_ol)  
    
    
    log_1_ol=np.log(1-ol)
    error_function_param2=np.multiply((1-yl),log_1_ol)  
    sum_error_function=error_function_param1 + error_function_param2
    sum = np.sum(sum_error_function)
    divisor=(-1)*len(training_data)
    error_function=sum/divisor

    #Error Function ends
    
    #Regularization Start
    
    reg_func_param2 = np.sum(w1*w1)
    reg_func_param3 = np.sum(w2*w2)    
    
    reg_func_param4 = (lambdaval/(2*len(training_data)))*(reg_func_param2 + reg_func_param3)
    reg_func = error_function + reg_func_param4
    
    #w1_without_bias = w1[:,0:n_input]
    grad_w1 = (grad_w1 + (lambdaval*w1))/len(training_data)
   # print grad_w1.shape
    #w2_without_bias = w2[:,0:n_input]
    grad_w2 = (grad_w2 + (lambdaval*w2))/len(training_data)
    #print grad_w2.shape    
    
    #grad_w1 = grad_w1[:, 0:n_input]
    #grad_w2 = grad_w2[:, 0:n_hidden] 
    #print grad_w1.shape
    #print grad_w2.shape   
    obj_val = reg_func
    print obj_val
    
    #Regularization End
    
    #Make sure you reshape the gradient matrices to a 1D array. for instance if your gradient matrices are grad_w1 and grad_w2
    #you would use code similar to the one below to create a flat array
    obj_grad = np.concatenate((grad_w1.flatten(), grad_w2.flatten()),0)
    #obj_grad = np.array([])
    
    return (obj_val,obj_grad)



def nnPredict(w1,w2,data):
    
    """% nnPredict predicts the label of data given the parameter w1, w2 of Neural
    % Network.

    % Input:
    % w1: matrix of weights of connections from input layer to hidden layers.
    %     w1(i, j) represents the weight of connection from unit i in input 
    %     layer to unit j in hidden layer.
    % w2: matrix of weights of connections from hidden layer to output layers.
    %     w2(i, j) represents the weight of connection from unit i in input 
    %     layer to unit j in hidden layer.
    % data: matrix of data. Each row of this matrix represents the feature 
    %       vector of a particular image
       
    % Output: 
    % label: a column vector of predicted labels""" 
    
    labels = np.zeros((data.shape[0],1))
    #Your code here
    
    #Feed Forward Starts
    data_bias_node=np.zeros(len(data))
    data_bias_node.fill(1)    
    data=np.column_stack([data,data_bias_node])   
    #print data.shape
    w1_t=np.transpose(w1)
    #print w1.shape
    aj=np.dot(data,w1_t)
    #print aj.shape
    zj=sigmoid(aj) 
   
    zj_bias_node=np.zeros(len(data))
    zj_bias_node.fill(1)    
    zj=np.column_stack([zj,zj_bias_node])
    #print zj.shape
    w2_t=np.transpose(w2)
    #print w2.shape      
    bl=np.dot(zj,w2_t)
    #print bl.shape
    ol=sigmoid(bl)
   
    #Feed Forward Ends
    
    #Label Prediction Start
    
    for x in range(ol.shape[0]):
       max_index = np.argmax(ol[x])
       labels[x] = max_index
       #print max_index
                     
    
    #Label Prediction End
    
    return labels
    



"""**************Neural Network Script Starts here********************************"""


train_data, train_label, validation_data,validation_label, test_data, test_label = preprocess();


#  Train Neural Network

# set the number of nodes in input unit (not including bias unit)
n_input = train_data.shape[1]; 

# set the number of nodes in hidden unit (not including bias unit)
n_hidden = 50;
				   
# set the number of nodes in output unit
n_class = 10;				   

# initialize the weights into some random matrices
initial_w1 = initializeWeights(n_input, n_hidden);
initial_w2 = initializeWeights(n_hidden, n_class);

# unroll 2 weight matrices into single column vector
initialWeights = np.concatenate((initial_w1.flatten(), initial_w2.flatten()),0)

# set the regularization hyper-parameter
lambdaval = 0.1;


args = (n_input, n_hidden, n_class, train_data, train_label, lambdaval)

#Train Neural Network using fmin_cg or minimize from scipy,optimize module. Check documentation for a working example

opts = {'maxiter' : 50}    # Preferred value.

nn_params = minimize(nnObjFunction, initialWeights, jac=True, args=args,method='CG', options=opts)

#In Case you want to use fmin_cg, you may have to split the nnObjectFunction to two functions nnObjFunctionVal
#and nnObjGradient. Check documentation for this function before you proceed.
#nn_params, cost = fmin_cg(nnObjFunctionVal, initialWeights, nnObjGradient,args = args, maxiter = 50)


#Reshape nnParams from 1D vector into w1 and w2 matrices
w1 = nn_params.x[0:n_hidden * (n_input + 1)].reshape( (n_hidden, (n_input + 1)))
w2 = nn_params.x[(n_hidden * (n_input + 1)):].reshape((n_class, (n_hidden + 1)))


#Test the computed parameters

predicted_label = nnPredict(w1,w2,train_data)
train_label = train_label.reshape(50000,1)
validation_label = validation_label.reshape(10000,1)
test_label = test_label.reshape(10000,1)
#find the accuracy on Training Dataset
#print predicted_label.shape
#print train_label.shape
print('\n Training set Accuracy:' + str(100*np.mean((predicted_label == train_label).astype(float))) + '%')

predicted_label = nnPredict(w1,w2,validation_data)

#find the accuracy on Validation Dataset

print('\n Validation set Accuracy:' + str(100*np.mean((predicted_label == validation_label).astype(float))) + '%')


predicted_label = nnPredict(w1,w2,test_data)

#find the accuracy on Validation Dataset

print('\n Test set Accuracy:' +  str(100*np.mean((predicted_label == test_label).astype(float))) + '%')
t1 = time.time()

total = t1-t0
print total

param_pickle=open('D:\Nikhil\Savefiles\Downloads\CSE 14\ML CSE574\PA1\param.pickle','wb')
pickle.dump([n_hidden,w1,w2,lambdaval], param_pickle)
param_pickle.close()