function total_params = dlm_basic_eval(bnet, dlmObject, FileNum)
% Zach A. Pardos (zp@csail.mit.edu)
% Bayesian Prediction Analysis for Dynamic Learning Maps (Kansas)
% in collaboration with Neil Heffernan and colleagues  (WPI)
% data property of Angela Broaddus (Kansas) and Neal Kingston (Kansas)


%number of nodes in the DAG
N=size(bnet.dag,1);

total_params=0;
for C=1:N
 bnet.CPD{C} = tabular_CPD(bnet, C);
 cpt = CPD_to_CPT(bnet.CPD{C});
 total_params = total_params + length(cpt(:));
end
total_params =total_params/2;


cases = cell(size(dlmObject.responses,1),N);
%observed nodes (last nodes for items)
cases(:,bnet.observed) = num2cell(dlmObject.responses+1);

engine = jtree_inf_engine(bnet);
max_iter = 300;
filename = 'predictions.txt';
report = fopen(filename,'w');
for fold=1:5
    %~80 percent training fold for student folds, : = all students, 1 =
    %all items(all items are the same for each student), 1 = student fold
    cases2=cases(dlmObject.folds(:,1,1) ~= fold, :);
    [bnet2, LLtrace, engine2] = learn_params_em(engine,cases2',max_iter);

    %test set, opposite of above training set for students
    cases3=cases(dlmObject.folds(:,1,1) == fold, :);
    
    studentIndices=zeros(size(cases3,1), 1);
    indexSpot=1;
    sizeArray = size(dlmObject.folds(:, 1));
    for student=1:sizeArray(1)
        if(dlmObject.folds(student,1,1) == fold)
            studentIndices(indexSpot) = student;
            indexSpot=indexSpot+1;
        end
    end
    
  
    %student variable goes from 1 - number of students in the fold
    %student variable is not actually the student number
    for student=1:size(cases3,1)
   
    
	fprintf('%d%% done with fold %d of 5\n',round(student*100/size(cases3,1)),fold);
        %get all items for a given student (c)
        case1=cases3(student,:);
        for ifold=1:3
            scase=cell(1,N);
            % for each student, for each item, 2 = item fold
            
            %evitems=cell(size(dlmObject.responses, 2));
            i= 1;
            j=1;
            
            %evitems = [];
            titems = [];
            %for each item, get a 1 if the items is in the fold and the item
            % index if the item index if that item is in the test set.
            for itemNumber=1:size(dlmObject.responses, 2)
                if(dlmObject.folds(studentIndices(student),itemNumber, 2) ~= ifold)
                    evitems(i) = 1;
                else
                    evitems(i) = 0;
                    %item indices
                    titems(j) = itemNumber;
                    j=j+1;
                end
                i = i+1;
            end
            
            evitems = logical(evitems);
            
            %return the evidence items
            scase(bnet.observed(evitems)) = case1(bnet.observed(evitems));
            [engine3,ll] = enter_evidence(engine2,scase);
            %get the predictions for the test items.
            for t=titems
                m = marginal_nodes(engine3,bnet.observed(t));
                m = m.T(2);
                fprintf(report,'%d %d %d %.5f %d\n',fold,studentIndices(student),t,m,case1{bnet.observed(t)}-1);
            end
        end
        
     
        
    end
end
save('bnet2.mat', 'bnet');
fclose(report);



