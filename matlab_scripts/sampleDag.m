%The function name should correspond to the name of the file. Please remember to make it match the model and iteration numbers
function [bnet, UID] = sampleDag()
% the total number of nodes in the model. This should be the sum of the skill and item nodes
N = 27;
numberOfItems = 8;
numberOfSkills = 3;
UID = 0;
% variable names for the skills in the graph. In this model we have only five skills. We could have several.
Skill1xSkill2_split_3 = 1;
Skill2 = 2;
Skill3 = 3;
% variable names for the guesses in the graph.
Guess1 = 4;
Guess2 = 5;
Guess3 = 6;
Guess4 = 7;
Guess5 = 8;
Guess6 = 9;
Guess7 = 10;
Guess8 = 11;
% variable names for the slips in the graph. 
 Slip1 = 12;
 Slip2 = 13;
 Slip3 = 14;
 Slip4 = 15;
 Slip5 = 16;
 Slip6 = 17;
 Slip7 = 18;
 Slip8 = 19;
% variable names for the item nodes(observable) and their node numbers
Item1 = 20;
Item2 = 21;
Item3 = 22;
Item4 = 23;
Item5 = 24;
Item6 = 25;
Item7 = 26;
Item8 = 27;
% topology is defined in a directed acyclic graph
dag = zeros(N,N);
% the following represent the links between the skill nodes
dag(Skill1xSkill2_split_3,Skill2) = 1;
dag(Skill2,Skill3) = 1;
% the following represent the links between the guess and item nodes
dag(Guess1,Item1) = 1;
dag(Guess2,Item2) = 1;
dag(Guess3,Item3) = 1;
dag(Guess4,Item4) = 1;
dag(Guess5,Item5) = 1;
dag(Guess6,Item6) = 1;
dag(Guess7,Item7) = 1;
dag(Guess8,Item8) = 1;
% the following represent the links between the slip and item nodes
dag( Slip1,Item1) = 1;
dag( Slip2,Item2) = 1;
dag( Slip3,Item3) = 1;
dag( Slip4,Item4) = 1;
dag( Slip5,Item5) = 1;
dag( Slip6,Item6) = 1;
dag( Slip7,Item7) = 1;
dag( Slip8,Item8) = 1;
% the following represent the links between the skill and item nodes
dag(Skill1xSkill2_split_3,Item1) = 1;
dag(Skill1xSkill2_split_3,Item2) = 1;
dag(Skill1xSkill2_split_3,Item3) = 1;
dag(Skill1xSkill2_split_3,Item4) = 1;
dag(Skill2,Item5) = 1;
dag(Skill3,Item6) = 1;
dag(Skill3,Item7) = 1;
dag(Skill3,Item8) = 1;
% equivalence classes specify which nodes share a single CPT.
eclass = zeros(1, N);
eclass = 1:N;
% observed variables. This should correspond to the item nodes.
obs = 20:27;
node_sizes = 2*ones(1,N);
discrete_nodes = 1:N;
bnet = mk_bnet(dag,node_sizes,'discrete',discrete_nodes,'observed',obs,'equiv_class',eclass);
bnet.CPD{1} = tabular_CPD(bnet, 1,  'CPT', [ 0.34 0.66 ]);
bnet.CPD{2} = tabular_CPD(bnet, 2,  'CPT', [ 0.84 0.24 0.16 0.76 ]);
bnet.CPD{3} = tabular_CPD(bnet, 3,  'CPT', [ 0.66 0.59 0.34 0.41 ]);
bnet.CPD{4} = tabular_CPD(bnet, 4,  'CPT', [ 0.74 0.26 ]);
bnet.CPD{5} = tabular_CPD(bnet, 5,  'CPT', [ 0.72 0.28 ]);
bnet.CPD{6} = tabular_CPD(bnet, 6,  'CPT', [ 0.87 0.13 ]);
bnet.CPD{7} = tabular_CPD(bnet, 7,  'CPT', [ 0.72 0.28 ]);
bnet.CPD{8} = tabular_CPD(bnet, 8,  'CPT', [ 0.71 0.29 ]);
bnet.CPD{9} = tabular_CPD(bnet, 9,  'CPT', [ 0.8 0.2 ]);
bnet.CPD{10} = tabular_CPD(bnet, 10,  'CPT', [ 0.71 0.29 ]);
bnet.CPD{11} = tabular_CPD(bnet, 11,  'CPT', [ 0.87 0.13 ]);
bnet.CPD{12} = tabular_CPD(bnet, 12,  'CPT', [ 0.87 0.13 ]);
bnet.CPD{13} = tabular_CPD(bnet, 13,  'CPT', [ 0.94 0.06 ]);
bnet.CPD{14} = tabular_CPD(bnet, 14,  'CPT', [ 0.89 0.11 ]);
bnet.CPD{15} = tabular_CPD(bnet, 15,  'CPT', [ 0.89 0.11 ]);
bnet.CPD{16} = tabular_CPD(bnet, 16,  'CPT', [ 0.91 0.09 ]);
bnet.CPD{17} = tabular_CPD(bnet, 17,  'CPT', [ 0.93 0.07 ]);
bnet.CPD{18} = tabular_CPD(bnet, 18,  'CPT', [ 0.88 0.12 ]);
bnet.CPD{19} = tabular_CPD(bnet, 19,  'CPT', [ 0.81 0.19 ]);
bnet.CPD{20} = tabular_CPD(bnet, 20,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{21} = tabular_CPD(bnet, 21,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{22} = tabular_CPD(bnet, 22,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{23} = tabular_CPD(bnet, 23,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{24} = tabular_CPD(bnet, 24,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{25} = tabular_CPD(bnet, 25,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{26} = tabular_CPD(bnet, 26,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{27} = tabular_CPD(bnet, 27,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
