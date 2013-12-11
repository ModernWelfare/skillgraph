 
clc;clear all;close all;

seed = 1;
s = RandStream('mt19937ar', 'Seed', seed);
RandStream.setGlobalStream(s);






%% load data
load dlmObject.mat;
load bnet.mat;

filename = '../results/results1.txt';
 
results = fopen(filename,'w');

%graph number given the current iteration
i = 1;

 
    %% Evaluate DAG
 
    para = dlm_basic_eval(bnet, dlmObject, i);
    data = load(['dlm_resultsFinalRun',num2str(i),'.txt']);
 
    %% Record Results
    aic = aic(data(:,5),data(:,4), para);
    bic = bic(data(:,5),data(:,4), para);
 
    aucR(i) = auc(data(:,4),data(:,5));
    rmseR(i) = sqrt(mean((data(:,4)-data(:,5)).^2));
    accuracyR(i) =  mean(round(data(:,4))==data(:,5));
    
    fprintf(results, '%d %.5f %.5f %.5f %.5f %.5f\n',i,aucR(i),rmseR(i),accuracyR(i), aic, bic)
   
 
fclose(results);
 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
