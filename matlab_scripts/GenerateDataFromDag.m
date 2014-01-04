%
%Generating the data file
%

function GenerateDataFromDag()

[bnet, UID, numberOfStudents] = currentDag();
a = size(bnet.observed);
numberOfItems = a(2);

%file name for the output
saveFile = ['UID', num2str(UID), 'Data', num2str(numberOfStudents), '.csv'];
saveFile2 = ['UID', num2str(UID), 'LatentData', num2str(numberOfStudents), '.csv'];

fileOutput = fopen(saveFile,'w');
fileOutput2 = fopen(saveFile2,'w');

for i=1:numberOfStudents
    
    responseValue = sample_bnet(bnet);
    numberOfNodes = size(bnet.dag, 1);
  
    for j=1:numberOfNodes-numberOfItems
          fprintf(fileOutput2,'%d,', responseValue{j}-1);
    end

    for j=numberOfNodes-numberOfItems+1:numberOfNodes
        
        %generate response from bayes net
        %print with comma or without if last one
        if(j ~= numberOfItems)
          fprintf(fileOutput,'%d,', responseValue{j}-1);
          fprintf(fileOutput2,'%d,', responseValue{j}-1);
        else
          fprintf(fileOutput,'%d', responseValue{j}-1);
          fprintf(fileOutput2,'%d,', responseValue{j}-1);
        end
    end
    fprintf(fileOutput,'\n');
    fprintf(fileOutput2,'\n');
end

fclose(fileOutput);
fclose(fileOutput2);

%save network object
saveFile3 = 'bnet.mat';
save(saveFile3, 'bnet');





