 function [modCSI, CSI] = csi(RR, N)
    % Using the HRV (RR) as input calculate CSI and modified CSI slope
    %Calculating ModCSI100filtered*slope and CSI100*slope:

    % time-axis for moving slope calculation
    t = cumsum(RR);

    fact = 1 / sqrt(2);

    CSI = [];
    modCSI = [];

    % calculate slope, csi and modified csi for each RR window
    for i = 1:length(RR) - N
        sd1 = std(fact * (RR(i:i + N - 1) - RR(i + 1:i + N)));
        sd2 = std(fact * (RR(i:i + N - 1) + RR(i + 1:i + N)));
        slope = abs(polyfit(t(i:i + N), RR(i:i + N), 1));
        CSI(end + 1) = sd2 / sd1;% * slope(1);
        modCSI(end + 1) = sd2 * sd2 / sd1;% * slope(1);
    end
end
