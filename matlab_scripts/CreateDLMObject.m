
%takes a file of the item data (Q-matrix) and creates a dlm object with folds from it

%clear everything
clc;clear all;close all;

seed = 3;

%set random seed to seed in this case (will give us the same set of random numbers everytime)
s = RandStream('mt19937ar', 'Seed', seed);
RandStream.setGlobalStream(s);

%
%Generating the data file
%

numberOfStudents = 100;


[bnet, UID] = sampleDag();
GenerateDataFromDag(bnet, numberOfStudents, UID);


%
%Create the dlmObject
%


%load the file with the item data

%make sure to change this in all places since filename = object name and
%object is used in more than one place
loadFile = ['UID', num2str(UID), 'Data', num2str(numberOfStudents), '.csv'];
data = load(loadFile);

%set the number of students and number of items based on the file
%rows = students, columns = items

numberOfStudents = size(data, 1);
numberOfItems = size(data, 2);

%sets constants for number of student folds and number of item folds
numberOfStudentFolds = 5;
numberOfItemFolds = 3;

%initialize the arrays in the dlm object
responses = zeros(numberOfStudents, numberOfItems);

%folds for each student and each item assign a student and item fold
folds = zeros(numberOfStudents, numberOfItems, 2);

%set the responses, student folds and item folds
for i=1:numberOfStudents
    for j=1:numberOfItems
        responses(i, j) = data(i, j);
        folds(i, j, 1) = round(1 + (numberOfStudentFolds-1).*rand(1,1));
        folds(i, j, 2) = round(1 + (numberOfItemFolds-1).*rand(1,1));
    end
end


%create the dlm object and save it
dlmObject = struct('responses', responses, 'folds', folds);
save('dlmObject.mat', 'dlmObject');










