function total_params = dlm_basic_eval(bnet, dlmObject, FileNum)
% Zach A. Pardos (zp@csail.mit.edu)
% Bayesian Prediction Analysis for Dynamic Learning Maps (Kansas)
% in collaboration with Neil Heffernan and colleagues  (WPI)
% data property of Angela Broaddus (Kansas) and Neal Kingston (Kansas)


N=size(bnet.dag,1);

total_params=0;
for C=1:N
 bnet.CPD{C} = tabular_CPD(bnet, C);
 cpt = CPD_to_CPT(bnet.CPD{C});
 total_params = total_params + length(cpt(:));
end
total_params =total_params/2;


cases = cell(size(dlmObject.responses,1),N);
cases(:,bnet.observed) = num2cell(dlmObject.responses+1);

engine = jtree_inf_engine(bnet);
max_iter = 300;
filename = ['dlm_resultsFinalRun',num2str(FileNum),'.txt'];
report = fopen(filename,'w');
for fold=1:5
    cases2=cases(dlmObject.folds(:,1,1) ~= fold, :);
    [bnet2, LLtrace, engine2] = learn_params_em(engine,cases2',max_iter);

    cases3=cases(dlmObject.folds(:,1,1) == fold, :);
    for c=1:size(cases3,1)
	fprintf('%d%% done with fold %d of 5\n',round(c*100/size(cases3,1)),fold);
        case1=cases3(c,:);
        for ifold=1:3
            scase=cell(1,N);
            evitems=dlmObject.folds(c, 2, 2) ~= ifold;
            scase(bnet.observed(evitems)) = case1(bnet.observed(evitems));
            [engine3,ll] = enter_evidence(engine2,scase);
            titems=find(dlmObject.folds(c, 2, 2) == ifold);
            for t=titems'
                m = marginal_nodes(engine3,bnet.observed(t));
                m = m.T(2);
                fprintf(report,'%d %d %d %.5f %d\n',fold,c,t,m,case1{bnet.observed(t)}-1);
            end
        end
    end
end

fclose(report);



