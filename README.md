# SortingCompetitionMaterials2016

Materials for UMM CSci 3501 "Algorithms and computability" 2016 sorting competition.

## Goal of the competition

The Sorting Competition is a multi-lab exercise on developing the fastest sorting algorithm for a given type of data. By "fast" we mean the actual running time and not the Big-Theta approximation. The solutions are developed in Java and will be ran on a single processor.

## The data

The data file starts with coordinates of coordinates two points on a grid of integers that we will refer to as *reference points*. The size of the grid is referred to as `gridSize` and will be between `50000` and `200000`. Each point is given on its own line; its X coordinate followed by the Y coordinate, separated by spaces. 

The data to be sorted consists of triples of integers. The first two elements are X and Y coordinates of each point to be sorted, followed by a timestamp, which also can be viewed just as the number of the point in the sequence, starting with 0 and up `N - 1` (where `N` is the total number of points to be sorted). 

## How do you need to sort the data 

The points are sorted based on their distance to **the closer** of the two reference points. If the distance is smaller than the threshold `0.0000000001`, it is considered zero. If the two points being compared have the difference of their distances to the closest reference point within the treshold, they are considered to be at the equal distance. In this case the point with the smaller timestamp is considered smaller. 

## How is the data generated

