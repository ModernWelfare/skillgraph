%The fucntion name should correspond to the name of the file. Please remember to make it match the model and iteration numbers
function [bnet, UID] = sampleDag()
% the total number of nodes in the model. This should be the sum of the skill and item nodes
N = 25;
numberOfItems = 8;
numberOfSkills = 1;
UID = 0;
% variable names for the skills in the graph. In this model we have only five skills. We could have several.
Skill1xSkill2xSkill2_split_3xSkill3 = 1;
% variable names for the guesses in the graph.
Guess1 = 2;
Guess2 = 3;
Guess3 = 4;
Guess4 = 5;
Guess5 = 6;
Guess6 = 7;
Guess7 = 8;
Guess8 = 9;
% variable names for the slips in the graph. 
 Slip1 = 10;
 Slip2 = 11;
 Slip3 = 12;
 Slip4 = 13;
 Slip5 = 14;
 Slip6 = 15;
 Slip7 = 16;
 Slip8 = 17;
% variable names for the item nodes(observable) and their node numbers
Item1 = 18;
Item2 = 19;
Item3 = 20;
Item4 = 21;
Item5 = 22;
Item6 = 23;
Item7 = 24;
Item8 = 25;
% topology is defined in a directed acyclic graph
dag = zeros(N,N);
% the following represent the links between the skill nodes
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
dag(Skill1xSkill2xSkill2_split_3xSkill3,Item1) = 1;
dag(Skill1xSkill2xSkill2_split_3xSkill3,Item2) = 1;
dag(Skill1xSkill2xSkill2_split_3xSkill3,Item3) = 1;
dag(Skill1xSkill2xSkill2_split_3xSkill3,Item5) = 1;
dag(Skill1xSkill2xSkill2_split_3xSkill3,Item4) = 1;
dag(Skill1xSkill2xSkill2_split_3xSkill3,Item6) = 1;
dag(Skill1xSkill2xSkill2_split_3xSkill3,Item7) = 1;
dag(Skill1xSkill2xSkill2_split_3xSkill3,Item8) = 1;
% equivalence classes specify which nodes share a single CPT.
eclass = zeros(1, N);
eclass = 1:N;
% observed variables. This should correspond to the item nodes.
obs = 18:25;
node_sizes = 2*ones(1,N);
discrete_nodes = 1:N;
bnet = mk_bnet(dag,node_sizes,'discrete',discrete_nodes,'observed',obs,'equiv_class',eclass);
bnet.CPD{1} = tabular_CPD(bnet, 1,  'CPT', [ 0.37 0.63 ]);
bnet.CPD{2} = tabular_CPD(bnet, 2,  'CPT', [ 0.74 0.26 ]);
bnet.CPD{3} = tabular_CPD(bnet, 3,  'CPT', [ 0.72 0.28 ]);
bnet.CPD{4} = tabular_CPD(bnet, 4,  'CPT', [ 0.87 0.13 ]);
bnet.CPD{5} = tabular_CPD(bnet, 5,  'CPT', [ 0.72 0.28 ]);
bnet.CPD{6} = tabular_CPD(bnet, 6,  'CPT', [ 0.71 0.29 ]);
bnet.CPD{7} = tabular_CPD(bnet, 7,  'CPT', [ 0.8 0.2 ]);
bnet.CPD{8} = tabular_CPD(bnet, 8,  'CPT', [ 0.71 0.29 ]);
bnet.CPD{9} = tabular_CPD(bnet, 9,  'CPT', [ 0.87 0.13 ]);
bnet.CPD{10} = tabular_CPD(bnet, 10,  'CPT', [ 0.87 0.13 ]);
bnet.CPD{11} = tabular_CPD(bnet, 11,  'CPT', [ 0.94 0.06 ]);
bnet.CPD{12} = tabular_CPD(bnet, 12,  'CPT', [ 0.89 0.11 ]);
bnet.CPD{13} = tabular_CPD(bnet, 13,  'CPT', [ 0.89 0.11 ]);
bnet.CPD{14} = tabular_CPD(bnet, 14,  'CPT', [ 0.91 0.09 ]);
bnet.CPD{15} = tabular_CPD(bnet, 15,  'CPT', [ 0.93 0.07 ]);
bnet.CPD{16} = tabular_CPD(bnet, 16,  'CPT', [ 0.88 0.12 ]);
bnet.CPD{17} = tabular_CPD(bnet, 17,  'CPT', [ 0.81 0.19 ]);
bnet.CPD{18} = tabular_CPD(bnet, 18,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{19} = tabular_CPD(bnet, 19,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{20} = tabular_CPD(bnet, 20,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{21} = tabular_CPD(bnet, 21,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{22} = tabular_CPD(bnet, 22,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{23} = tabular_CPD(bnet, 23,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{24} = tabular_CPD(bnet, 24,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{25} = tabular_CPD(bnet, 25,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
