#!/bin/sh

for i in {1..12}
do
	cp gcd_run gcd_run_${i}
        chmod +x gcd_run_${i}
done
