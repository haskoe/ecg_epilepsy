 function [modCSI, CSI] = calc_csi(RRbf, N)
    % Using the HRV (RR) as input calculate CSI and modified CSI slope
    %Calculating ModCSI100filtered*slope and CSI100*slope:


    fact = 1 / sqrt(2);

    CSI = [];
    modCSI = [];
    
    %Median filter, using the median of the previous 7 RR intervals:
    
    RR=movmedian(RRbf, [7 0]);
    
    % time-axis for moving slope calculation for modCSI
    tm = cumsum(RR);

    a = [];
    % calculate slope and modified csi for each RR window
    for i = 1:length(RR) - N
        sd1mod = std(fact * ((RR(i:i + N - 1) - RR(i + 1:i + N))));
        sd2mod = std(fact * ((RR(i:i + N - 1) + RR(i + 1:i + N))));
        slope = abs(polyfit(tm(i:i + N), RR(i:i + N), 1));
        modCSI(end + 1) = 4*sd2mod * sd2mod / sd1mod * slope(1);
        a(end+1) = slope(1);
    end
    
  % calculation of csi

    for i = 1:length(RRbf) - N
        sd1 = std(fact * ((RRbf(i:i + N - 1) - RRbf(i + 1:i + N))));
        sd2 = std(fact * ((RRbf(i:i + N - 1) + RRbf(i + 1:i + N))));
        CSI(end + 1) = sd2 / sd1 * a(i);
    end   
 end

