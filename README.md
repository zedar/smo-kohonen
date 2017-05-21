# Draw (animate results)

start gnuplot in project folder

    gnuplot> set datafile separator ','
    gnuplot> do for [i=0:19] {plot sprintf('out/iter_%d.txt', i) with points pointtype 7 pointsize 1 lc rgb 'blue'; pause 0.5}
    
    gnuplot> do for [i=0:10] {plot sprintf('data/dane_test.txt') with points pointtype 7 pointsize 1 lc rgb 'blue', sprintf('out/iter_%d.txt', i*10) with points pointtype 7 pointsize 1 lc rgb 'red'; pause 0.5}