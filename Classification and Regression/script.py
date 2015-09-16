import numpy as np
from scipy.optimize import minimize
from scipy.io import loadmat
from math import sqrt
import scipy.io
import matplotlib.pyplot as plt
import pickle

def ldaLearn(X,y):
    # Inputs
    # X - a N x d matrix with each row corresponding to a training example
    # y - a N x 1 column vector indicating the labels for each training example
    #
    # Outputs
    # means - A d x k matrix containing learnt means for each of the k classes
    # covmat - A single d x d learnt covariance matrix 
    
    # IMPLEMENT THIS METHOD
    
    means_temp = []
    classes = np.unique(y)
    for group in classes:
        Xg = X[y.flatten() == group, :]
        means_temp.append(Xg.mean(0))
        
    means=np.transpose(np.asarray(means_temp))
    global_mean=np.mean(X,axis=0)
    
    mean_corrected_data = []
    for group in classes:
        Xg = X[y.flatten() == group, :]
        mean_corrected_data.append(Xg - global_mean)
    
    covmats = []
    for group in classes:
        covmats.append((np.dot(np.transpose(mean_corrected_data[int(group)-1]),mean_corrected_data[int(group)-1]))/(mean_corrected_data[int(group)-1].size/2))
    
    covmat=np.zeros((covmats[0].shape))
    for group in classes:
        multiplier=(mean_corrected_data[int(group)-1].size/2.0)/(X.size/2.0)
        covmat=covmat+((covmats[int(group)-1])*multiplier)
    
    return means,covmat

def qdaLearn(X,y):
    # Inputs
    # X - a N x d matrix with each row corresponding to a training example
    # y - a N x 1 column vector indicating the labels for each training example
    #
    # Outputs
    # means - A d x k matrix containing learnt means for each of the k classes
    # covmats - A list of k d x d learnt covariance matrices for each of the k classes
    
    # IMPLEMENT THIS METHOD
    means_temp = []
    classes = np.unique(y)
    for group in classes:
        Xg = X[y.flatten() == group, :]
        means_temp.append(Xg.mean(0))
        
    means=np.transpose(np.asarray(means_temp))
    global_mean=np.mean(X,axis=0)
    
    mean_corrected_data = []
    for group in classes:
        Xg = X[y.flatten() == group, :]
        mean_corrected_data.append(Xg - global_mean)
    
    covmats = []
    for group in classes:
        Xg = X[y.flatten() == group, :]
        Xg = Xg - global_mean
        covmats.append(np.cov(Xg, rowvar =0))     
    return means,covmats

def ldaTest(means,covmat,Xtest,ytest):
    # Inputs
    # means, covmat - parameters of the LDA model
    # Xtest - a N x d matrix with each row corresponding to a test example
    # ytest - a N x 1 column vector indicating the labels for each test example
    # Outputs
    # acc - A scalar accuracy value
    
    # IMPLEMENT THIS METHOD
    
    invcovmat=np.linalg.inv(covmat);
    covmatdet=np.linalg.det(covmat);
    pdf= np.zeros((Xtest.shape[0],means.shape[1]));
    for i in range(means.shape[1]):
        pdf[:,i] = np.exp(-0.5*np.sum((Xtest - means[:,i])*np.dot(invcovmat, (Xtest - means[:,i]).T).T,1))/(np.sqrt(np.pi*2)*(np.power(covmatdet,2)));
    acc = 100*np.mean((np.argmax(pdf,1)+1) == ytest.reshape(ytest.size));
    return acc

def qdaTest(means,covmats,Xtest,ytest):
    # Inputs
    # means, covmats - parameters of the QDA model
    # Xtest - a N x d matrix with each row corresponding to a test example
    # ytest - a N x 1 column vector indicating the labels for each test example
    # Outputs
    # acc - A scalar accuracy value
    
    # IMPLEMENT THIS METHOD
    pdf= np.zeros((Xtest.shape[0],means.shape[1]))
    for eachValue in range(means.shape[1]):
        invcovmat = np.linalg.inv(covmats[eachValue]);
        covmatdet = np.linalg.det(covmats[eachValue]);
        pdf[:,eachValue] = np.exp(-0.5*np.sum((Xtest - means[:,eachValue])*np.dot(invcovmat, (Xtest - means[:,eachValue]).T).T,1))/(np.sqrt(np.pi*2)*(np.power(covmatdet,2)));
    acc = 100*np.mean((np.argmax(pdf,1)+1) == ytest.reshape(ytest.size)); 
    return acc

def learnOLERegression(X,y):
    # Inputs:                                                         
    # X = N x d 
    # y = N x 1                                                               
    # Output: 
    # w = d x 1                                                                
    # IMPLEMENT THIS METHOD  
    w =  np.dot(np.linalg.inv(np.dot(np.transpose(X),X)),np.dot(np.transpose(X),y))                                                 
    return w

def learnRidgeRegression(X,y,lambd):
    # Inputs:
    # X = N x d                                                               
    # y = N x 1 
    # lambd = ridge parameter (scalar)
    # Output:                                                                  
    # w = d x 1                                                                

    # IMPLEMENT THIS METHOD   
    I = np.eye(X.shape[1])    
    w =  np.dot(np.linalg.inv(np.add(np.dot(np.transpose(X),X),(X.shape[0]*lambd*I))),np.dot(np.transpose(X),y))                                                   
    return w

def testOLERegression(w,Xtest,ytest):
    # Inputs:
    # w = d x 1
    # Xtest = N x d
    # ytest = X x 1
    # Output:
    # rmse
    
    # IMPLEMENT THIS METHOD
    rmse=sqrt(np.sum((np.subtract(ytest,np.dot(Xtest,w))*np.subtract(ytest,np.dot(Xtest,w)))))/Xtest.shape[0]
    return rmse

def regressionObjVal(w, X, y, lambd):

    # compute squared error (scalar) and gradient of squared error with respect
    # to w (vector) for the given data X and y and the regularization parameter
    # lambda                                                                  

    # IMPLEMENT THIS METHOD
    
    w1=np.array([w]).T
    error=((np.dot(np.transpose(np.subtract(y,np.dot(X,w1))),np.subtract(y,np.dot(X,w1))))/(2.0*X.shape[0])) + ((np.dot(lambd,np.dot(np.transpose(w1),w1)))/2)   
    error_grad=((-np.dot(np.transpose(y),X)+np.dot(np.transpose(w),np.dot(np.transpose(X),X)))/X.shape[0]) + np.dot(lambd,np.transpose(w))                                        
    return error.flatten(), error_grad.flatten()

def mapNonLinear(x,p):
    # Inputs:                                                                  
    # x - a single column vector (N x 1)                                       
    # p - integer (>= 0)                                                       
    # Outputs:                                                                 
    # Xd - (N x (d+1))                                                         
    # IMPLEMENT THIS METHOD
    
    x1=np.array([x]).T
    Xd=np.ones_like(x1) 
    for i in range(1,p+1):
       Xd=np.hstack((Xd,x1**i)) 
    return Xd

# Main script

# Problem 1
# load the sample data                                                                 
X,y,Xtest,ytest = pickle.load(open('E:\UB\CSE 574\Projects\PA2\sample.pickle','rb'))            
def plotGraph(Y,Z):
    colorList=['blue','yellow','red','orange','green']
    for i in range (Y.shape[0]):
       plt.scatter(Y[i,0],Y[i,1],c=colorList[int(Z[i])-1])
        
    plt.plot()
    
   
def generateMesh(means,covmat,qdaFlag):
    x1 = np.linspace(0,16,num=101)
    x2 = np.linspace(0,16,num=101)
    x=np.meshgrid(x1,x2)
    x=np.array(x)
    c=x[0].reshape(101*101,1)
    d=x[1].reshape(101*101,1)
    f=np.hstack((c,d))
    invcovmat = np.linalg.inv(covmat)
    covmatdet = np.linalg.det(covmat)
    if qdaFlag is False:
        pdf= np.zeros((f.shape[0],means.shape[1]))
        for i in range(means.shape[1]):
            pdf[:,i] = np.exp(-0.5*np.sum((f - means[:,i])* 
            np.dot(invcovmat, (f - means[:,i]).T).T,1))/(np.sqrt(np.pi*2)*(np.power(covmatdet,2)))
    else:
        pdf= np.zeros((f.shape[0],means.shape[1]))
        for i in range(means.shape[1]):
            invcovmat = np.linalg.inv(covmat[i])
            covmatdet = np.linalg.det(covmat[i])
            pdf[:,i] = np.exp(-0.5*np.sum((f - means[:,i])* 
            np.dot(invcovmat, (f - means[:,i]).T).T,1))/(np.sqrt(np.pi*2)*(np.power(covmatdet,2)))
    trueLabel = np.argmax(pdf,1)
    trueLabel = trueLabel + 1
    plotGraph(f,trueLabel)
    
# LDA
means,covmat = ldaLearn(X,y)
ldaacc = ldaTest(means,covmat,Xtest,ytest)
print('LDA Accuracy = '+str(ldaacc))
plt.figure()
generateMesh(means,covmat,False)
#generatePoints(Xtest,means,covmat,False)
plt.title("LDA")
plt.show()

# QDA
means,covmats = qdaLearn(X,y)
qdaacc = qdaTest(means,covmats,Xtest,ytest)
print('QDA Accuracy = '+str(qdaacc))
plt.figure()
generateMesh(means,covmats,True)
#generatePoints(Xtest,meansQda,covmatsQda,True)
#print('QDA Accuracy = '+str(qdaacc))
plt.title("QDA")
plt.show()

# Problem 2

X,y,Xtest,ytest = pickle.load(open('E:\UB\CSE 574\Projects\PA2\diabetes.pickle','rb'))   
# add intercept
X_i = np.concatenate((np.ones((X.shape[0],1)), X), axis=1)
Xtest_i = np.concatenate((np.ones((Xtest.shape[0],1)), Xtest), axis=1)

w = learnOLERegression(X,y)
mle = testOLERegression(w,Xtest,ytest)

w_i = learnOLERegression(X_i,y)
mle_i = testOLERegression(w_i,Xtest_i,ytest)

print('RMSE without intercept '+str(mle))
print('RMSE with intercept '+str(mle_i))

# Problem 3
k = 21
lambdas = np.linspace(0, 0.004, num=k)
i = 0
rmses3 = np.zeros((k,1))
for lambd in lambdas:
    w_l = learnRidgeRegression(X_i,y,lambd)
    rmses3[i] = testOLERegression(w_l,Xtest_i,ytest)
    i = i + 1
#plt.plot(lambdas,rmses3)
#plt.show()
# Problem 4
k = 21
lambdas = np.linspace(0, 0.004, num=k)
i = 0
rmses4 = np.zeros((k,1))
opts = {'maxiter' : 100}    # Preferred value.                                                
w_init = np.zeros((X_i.shape[1],1))
for lambd in lambdas:
    args = (X_i, y, lambd)
    w_l = minimize(regressionObjVal, w_init, jac=True, args=args,method='CG', options=opts)
    w_l_1 = np.zeros((X_i.shape[1],1))
    for j in range(len(w_l.x)):
        w_l_1[j] = w_l.x[j]
    rmses4[i] = testOLERegression(w_l_1,Xtest_i,ytest)
    i = i + 1
#plt.plot(lambdas,rmses4)
#plt.show()

# Problem 5
pmax = 7
lambda_opt = lambdas[np.argmin(rmses4)]
rmses5 = np.zeros((pmax,2))
for p in range(pmax):
    Xd = mapNonLinear(X[:,2],p)
    Xdtest = mapNonLinear(Xtest[:,2],p)
    w_d1 = learnRidgeRegression(Xd,y,0)
    rmses5[p,0] = testOLERegression(w_d1,Xdtest,ytest)
    w_d2 = learnRidgeRegression(Xd,y,lambda_opt)
    rmses5[p,1] = testOLERegression(w_d2,Xdtest,ytest)
#plt.plot(range(pmax),rmses5)
#plt.legend(('No Regularization','Regularization'))
#plt.show()