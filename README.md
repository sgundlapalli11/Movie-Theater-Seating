# Movie-Theater-Seating

How to run
```
javac Theatre.java
java Theatre absolute_path_to_repo/test/test_file#.txt
```

View Results
```
cat absolute_path_to_repo/output/test_file#_output.txt
```

Assumptions
- Only 1 client. Do not need to worry about multithreading and race conditions 
- Customer satisfaction is defined by:
  - wanting to sit with your group
  - seated towards middle
  - want to be far from screen
  - gaps between parties
- Reservation IDs passed in are unique. Do not need to worry about reservations being updated
- Reservation come in 1-by-1 so do not know how many total tickets are requested upfront
