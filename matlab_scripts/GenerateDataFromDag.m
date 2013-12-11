function GenerateDataFromDag(bnet, numberOfStudents, UID)

a = size(bnet.observed);
numberOfItems = a(2);

%file name for the output
saveFile = ['UID', num2str(UID), 'Data', num2str(numberOfStudents), '.csv'];
fileOutput = fopen(saveFile,'w');

for i=1:numberOfStudents
    
    responseValue = sample_bnet(bnet);
    numberOfNodes = size(bnet.dag, 1);
    
    for j=numberOfNodes-numberOfItems+1:numberOfNodes
        
        %generate response from bayes net
        %print with comma or without if last one
        if(j ~= numberOfItems)
          fprintf(fileOutput,'%d,', responseValue{j}-1);
        else
          fprintf(fileOutput,'%d', responseValue{j}-1);
        end
        
    end
    fprintf(fileOutput,'\n');
end

fclose(fileOutput);

%save network object
saveFile2 = 'bnet.mat';
save(saveFile2, 'bnet');





