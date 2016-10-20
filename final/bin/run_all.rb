### the script for running the 2016 sorting competition 

### to pin to one core, run as: taskset -c 0 ruby run_all.rb

require 'timeout'

def medianOfThree arr
  if arr[0] <= arr[1]
    if arr[1] <= arr[2]
      arr[1]
    elsif arr[2] <= arr[0] 
      arr[0]
    else 
      arr[2]
    end 
  else 
    if arr[0] <= arr[2]
      arr[0]
    elsif arr[2] <= arr[1] 
      arr[1]
    else 
      arr[2]
    end 
  end
end

groups = 13 # number of groups (including group 0 which is the sample sorting)
inFileNames = ["final1.txt", "final2.txt"]
#inFileNames = ["round2-data2.txt"]
runTimes = []
sortedTimes = []

# loop over the sets of data

inFileNames.length.times do |r| 
  # generate data, store it in a file
  resultsFile = "results#{r + 1}.txt"
  #system("java DataGenerator #{inFileNames[r]} #{elements[r]} #{lambdas[r]}")
  system("echo 'Running #{inFileNames[r]}\n' >> #{resultsFile}")
  runTimes[r] = []
 
  # run all groups
  groups.times do |j|
    system("echo 'Group #{j}\n'")
    system("echo '\n Group #{j}: \n' >> #{resultsFile}")
    runTimes[r][j] = []
    3.times do |k|
      printed = `java -Xms3G Group#{j} #{inFileNames[r]} outRun#{r + 1}Group#{j}.txt`
      #printed = `java Group#{j} #{inFileNames[r]} outRun#{r + 1}Group#{j}.txt`
      runTimes[r][j][k] = printed.to_f
      begin
      Timeout::timeout(30) do
        system("echo '#{printed}' >> #{resultsFile}")
        system("echo '#{printed}'")
      end
      rescue Timeout::Error
        runTimes[r][j][k] = 1000000.0
      end 
    end
    system("echo 'Median: #{medianOfThree(runTimes[r][j])}' >> #{resultsFile}")
    runTimes[r][j][3] = medianOfThree(runTimes[r][j]) #store the median as the last element 
    runTimes[r][j][4] = j #record the group number
    
    ### disqualify those whose diff is non-empty
    system("echo 'Results of diff:\n' >> #{resultsFile}")
    system("diff --ignore-all-space --brief outRun#{r + 1}Group#{j}.txt outRun#{r + 1}Group0.txt >> #{resultsFile}")

    # matching the stderr to check correctness:
    if $?.exitstatus == 1
      system("echo 'The files outRun#{r + 1}Group#{j}.txt outRun#{r + 1}Group0.txt differ'")
      # ignoring the times:
      runTimes[r][j][3] = 1000000.0
    else 
      system("echo 'Sorting is correct'")
    end

    # ignoring output that's not a number:
    if runTimes[r][j][3] == 0 
      runTimes[r][j][3] = 1000000.0 # a large number if a group doesn't print time properly
    end
  end
  # processing the results of the run
  #system("echo 'This is my array: #{runTimes[r]}'")
  sortedTimes[r] = (runTimes[r]).sort {|arr1, arr2| arr1[3] <=> arr2[3]}
  #system("echo 'This is sorted array: #{sortedTimes[r]}'")
end

# determine the winner

# results[g][0] is the group number, results[g][1] is the 
# sum of places, results[g][2] is the sum of medians

# initialize an array to zeros   
results = Array.new(groups) { |i| Array.new(3) { |i| 0 }}

# populate group numbers
(0..groups-1).each do |i|
  results[i][0] = i
end

# fill in the sums of places and medians
inFileNames.length.times do |r|
  sortedTimes[r].length.times do |s|
    results[sortedTimes[r][s][4]][1] += s + 1 # incrementing the group's total place by index in sortedTimes 
    results[sortedTimes[r][s][4]][2] += sortedTimes[r][s][3]
  end
end

system("echo 'By groups: #{results}'")

# sort
results.sort! {|group1, group2| if group1[1] == group2[1] then group1[2] <=> group2[2] else group1[1] <=> group2[1] end}

system("echo 'By winners: #{results}'")

# display the results and store them in a file
results.each_with_index {|group, place| 
  str = "group #{group[0]} took place  #{place + 1}. The sum of places is #{group[1]}, the sum of medians is #{group[2]}"
  system("echo '#{str}'")
  system("echo '#{str}\n' >> scoreboard.txt")
}
