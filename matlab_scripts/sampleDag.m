%The fucntion name should correspond to the name of the file. Please remember to make it match the model and iteration numbers
function [bnet, UID] = sampleDag() 

% the total number of nodes in the model. This should be the sum of the skill and item nodes and guess and slip
N = 8;
numberOfItems = 2;
numberOfSkills = 2;

% unique graph identifier
UID = 1;

% variable names for the skills in the graph. In this model we have only five skills. We could have several.

Skill1=1;
Skill2=2;



%variable names for the guess nodes in the graph.

Guess1=3;
Guess2=4;


%variable names for the slip nodes in the graph.

Slip1=5;
Slip2=6;


% variable names for the item nodes(observable) and their node numbers

Item1=7;
Item2=8;


% topology is defined in a directed acyclic graph
dag = zeros(N,N);

% the following represent the links between the skill nodes
dag(Skill1,Skill2) = 1;


% the following represent the links between the guess and item nodes
dag(Guess1,Item1) = 1;
dag(Guess2,Item2) = 1;


% the following represent the links between the slip and item nodes
dag(Slip1,Item1) = 1;
dag(Slip2,Item2) = 1;


% the following represent the links between the skill and item nodes
dag(Skill1,Item1) = 1;
dag(Skill2,Item2) = 1;


% equivalence classes specify which nodes share a single CPT.  
eclass = zeros(1,N);

% Skill1 gets a separate eclass because it has no parent (different CPT dimension)
% Skill1 CPT contains the prior probability
% eclass(Skill1) = 1;

% In fact each skill and each item will have it's own CPT dimension. In that case
% we will have the following:

eclass=1:N;

% observed variables. This should correspond to the item nodes.
%obs = (numberOfSkills + numberOfItems*2)+1:N;

obs = (numberOfSkills + numberOfItems*2)+1:N;


% all nodes modeled as binary variables
node_sizes = 2*ones(1,N);

% all nodes are discrete variables
discrete_nodes = 1:N;
%make the BayesNet
bnet = mk_bnet(dag,node_sizes,'discrete',discrete_nodes,'observed',obs,'equiv_class',eclass);

% Build CPT for each skill node. 

% Since each node has its own equivalence class, you will have to generate a line of code that looks like the one just 
% below this comment, but has different values. The values should be from the CPT tables stored in the SkillGraph object. 
% Take note that the row entries correspond to the entries in the CPT tables for that given skill.


%Skill with one parent
%bnet.CPD{1} = tabular_CPD(bnet, Skill1, 'CPT', [row_entry_2 row_entry_1]);
%Skill with two parents
%bnet.CPD{2} = tabular_CPD(bnet, Skill2, 'CPT', [row_entry_2 row_entry_3 row_entry_1 row_entry_4]);
% etc




%for each node, output their CPT table

bnet.CPD{1} = tabular_CPD(bnet, 1,  'CPT', [ .20 .80 ] );
bnet.CPD{2} = tabular_CPD(bnet, 2,  'CPT', [ .20 .80 .6 .4 ] );

bnet.CPD{3} = tabular_CPD(bnet, 3,  'CPT', [ .80 .20 ] );
bnet.CPD{4} = tabular_CPD(bnet, 4,  'CPT', [ .80 .20 ] );

bnet.CPD{5} = tabular_CPD(bnet, 5,  'CPT', [ .9 .1 ] );
bnet.CPD{6} = tabular_CPD(bnet, 6,  'CPT', [ .9 .1 ] );

bnet.CPD{7} = tabular_CPD(bnet, 7,  'CPT', [1 0 0 0 1 1 1 0 0 1 1 1 0 0 0 1] );
bnet.CPD{8} = tabular_CPD(bnet, 8,  'CPT', [1 0 0 0 1 1 1 0 0 1 1 1 0 0 0 1] );
%etc...


