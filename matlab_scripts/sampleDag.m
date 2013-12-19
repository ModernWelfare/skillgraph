%The fucntion name should correspond to the name of the file. Please remember to make it match the model and iteration numbers
function [bnet, UID] = sampleDag()
% the total number of nodes in the model. This should be the sum of the skill and item nodes
N = 7;
numberOfItems = 2;
numberOfSkills = 1;
UID = 0;
% variable names for the skills in the graph. In this model we have only five skills. We could have several.
Skill1xSkill1-split-1 = 1;
% variable names for the guesses in the graph.
Guess1 = 2;
Guess2 = 3;
% variable names for the slips in the graph. 
 Slip1 = 4;
 Slip2 = 5;
% variable names for the item nodes(observable) and their node numbers
Item1 = 6;
Item2 = 7;
% topology is defined in a directed acyclic graph
dag = zeros(N,N);
% the following represent the links between the skill nodes
% the following represent the links between the guess and item nodes
dag(Guess1,Item1) = 1;
dag(Guess2,Item2) = 1;
% the following represent the links between the slip and item nodes
dag( Slip1,Item1) = 1;
dag( Slip2,Item2) = 1;
% the following represent the links between the skill and item nodes
dag(Skill1xSkill1-split-1,Item2) = 1;
dag(Skill1xSkill1-split-1,Item1) = 1;
% equivalence classes specify which nodes share a single CPT.
eclass = zeros(1, N);
eclass = 1:N;
% observed variables. This should correspond to the item nodes.
obs = 6:7;
node_sizes = 2*ones(1,N);
discrete_nodes = 1:N;
bnet = mk_bnet(dag,node_sizes,'discrete',discrete_nodes,'observed',obs,'equiv_class',eclass);
bnet.CPD{1} = tabular_CPD(bnet, 1,  'CPT', [ 0.44999999999999996 0.55 ]);
bnet.CPD{2} = tabular_CPD(bnet, 2,  'CPT', [ 0.79 0.21 ]);
bnet.CPD{3} = tabular_CPD(bnet, 3,  'CPT', [ 0.79 0.21 ]);
bnet.CPD{4} = tabular_CPD(bnet, 4,  'CPT', [ 0.83 0.17 ]);
bnet.CPD{5} = tabular_CPD(bnet, 5,  'CPT', [ 0.9 0.1 ]);
bnet.CPD{6} = tabular_CPD(bnet, 6,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
bnet.CPD{7} = tabular_CPD(bnet, 7,  'CPT', [ 1.0 0.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 1.0 1.0 1.0 0.0 0.0 0.0 1.0 ]);
