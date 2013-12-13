%The fucntion name should correspond to the name of the file. Please remember to make it match the model and iteration numbers
function [bnet, UID] = sampleDag()
% the total number of nodes in the model. This should be the sum of the skill and item nodes
N = 16;
numberOfItems = 4;
numberOfSkills = 4;
UID = 2;
% variable names for the skills in the graph. In this model we have only five skills. We could have several.
Skill1 = 1;
Skill2 = 2;
Skill3 = 3;
Skill4 = 4;
% variable names for the guesses in the graph.
Guess1 = 5;
Guess2 = 6;
Guess3 = 7;
Guess4 = 8;
% variable names for the slips in the graph. 
 Slip1 = 9;
 Slip2 = 10;
 Slip3 = 11;
 Slip4 = 12;
% variable names for the item nodes(observable) and their node numbers
Item1 = 13;
Item2 = 14;
Item3 = 15;
Item4 = 16;
% topology is defined in a directed acyclic graph
dag = zeros(N,N);
% the following represent the links between the skill nodes
dag(Skill1,Skill2) = 1;
dag(Skill1,Skill3) = 1;
dag(Skill2,Skill4) = 1;
dag(Skill3,Skill4) = 1;
% the following represent the links between the guess and item nodes
dag(Guess1,Item1) = 1;
dag(Guess2,Item2) = 1;
dag(Guess3,Item3) = 1;
dag(Guess4,Item4) = 1;
% the following represent the links between the slip and item nodes
dag( Slip1,Item1) = 1;
dag( Slip2,Item2) = 1;
dag( Slip3,Item3) = 1;
dag( Slip4,Item4) = 1;
% the following represent the links between the skill and item nodes
dag(Skill1,Item1) = 1;
dag(Skill2,Item2) = 1;
dag(Skill3,Item3) = 1;
dag(Skill4,Item4) = 1;
% equivalence classes specify which nodes share a single CPT.
eclass = zeros(1, N);
eclass = 1:N;
% observed variables. This should correspond to the item nodes.
obs = 13:16;
node_sizes = 2*ones(1,N);
discrete_nodes = 1:N;
bnet = mk_bnet(dag,node_sizes,'discrete',discrete_nodes,'observed',obs,'equiv_class',eclass);
bnet.CPD{1} = tabular_CPD(bnet, 1,  'CPT', [ 0.21 0.79 ]);
bnet.CPD{2} = tabular_CPD(bnet, 2,  'CPT', [ 0.88 0.57 0.12 0.43 ]);
bnet.CPD{3} = tabular_CPD(bnet, 3,  'CPT', [ 0.67 0.48 0.33 0.52 ]);
bnet.CPD{4} = tabular_CPD(bnet, 4,  'CPT', [ 0.82 0.89 0.5 0.09 0.18 0.11 0.5 0.91 ]);
bnet.CPD{5} = tabular_CPD(bnet, 5,  'CPT', [ 0.72 0.28 ]);
bnet.CPD{6} = tabular_CPD(bnet, 6,  'CPT', [ 0.87 0.13 ]);
bnet.CPD{7} = tabular_CPD(bnet, 7,  'CPT', [ 0.72 0.28 ]);
bnet.CPD{8} = tabular_CPD(bnet, 8,  'CPT', [ 0.71 0.29 ]);
bnet.CPD{9} = tabular_CPD(bnet, 9,  'CPT', [ 0.87 0.13 ]);
bnet.CPD{10} = tabular_CPD(bnet, 10,  'CPT', [ 0.81 0.19 ]);
bnet.CPD{11} = tabular_CPD(bnet, 11,  'CPT', [ 0.93 0.07 ]);
bnet.CPD{12} = tabular_CPD(bnet, 12,  'CPT', [ 0.87 0.13 ]);
bnet.CPD{13} = tabular_CPD(bnet, 13,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{14} = tabular_CPD(bnet, 14,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{15} = tabular_CPD(bnet, 15,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{16} = tabular_CPD(bnet, 16,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
