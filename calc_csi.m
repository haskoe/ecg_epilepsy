function csi = calc_csi(patient, rr, N)
    % Using the HRV (RR) as input calculate CSI and modified CSI slope
    %Calculating ModCSI100filtered*slope and CSI100*slope:
    fact = 1 / sqrt(2);

    CSI = [];
    modCSI = [];
    
    RRbf = rr.rr;

    %Median filter, using the median of the previous 7 RR intervals:
    RR=movmedian(rr.rr, [7 0]);
    
    % time-axis for moving slope calculation for modCSI
    tm = cumsum(RR);

    a = [];
    % calculate slope and modified csi for each RR window
    end_idx=length(RR) - N
    for i = 1:end_idx
        sd1mod = std(fact * ((RR(i:i + N - 1) - RR(i + 1:i + N))));
        sd2mod = std(fact * ((RR(i:i + N - 1) + RR(i + 1:i + N))));
        slope = abs(polyfit(tm(i:i + N), RR(i:i + N), 1));
        modCSI(end + 1) = 4*sd2mod * sd2mod / sd1mod * slope(1);
        a(end+1) = slope(1);
    end
    
    % calculation of csi
    for i = 1:end_idx
        sd1 = std(fact * ((RRbf(i:i + N - 1) - RRbf(i + 1:i + N))));
        sd2 = std(fact * ((RRbf(i:i + N - 1) + RRbf(i + 1:i + N))));
        CSI(end + 1) = sd2 / sd1 * a(i);
    end   

  csi.CSI = CSI;
  csi.modCSI = modCSI;

  % estimated time axis
  time_start_idx=N/2;

  csi.t = tm(time_start_idx:time_start_idx+length(CSI)-1);
end
