str = 'This is the matrix: ';      %# A string
pause(5);
fName = 'str_and_mat.txt';         %# A file name
fid = fopen(fName,'w');            %# Open the file
if fid ~= -1
  fprintf(fid,'%s\r\n',str);       %# Print the string
  fclose(fid);                     %# Close the file
end
exit();