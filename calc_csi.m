function csi = calc_csi(patient, params, rr, N)
    % Using the HRV (RR) as input calculate CSI and modified CSI slope
    %Calculating ModCSI100filtered*slope and CSI100*slope:
    fact = 1 / sqrt(2);

    CSI = [];
    modCSI = [];
    
    RRbf = rr;

    %Median filter, using the median of the previous 7 RR intervals:
    RR=movmedian(rr, [7 0]);
    
    % time-axis for moving slope calculation for modCSI
    tm = cumsum(RR);

    a = [];
    % calculate slope and modified csi for each RR window
    end_idx=length(RR) - N;
    modCSI=ones(1,end_idx);
    for i = 1:end_idx
        sd1mod = std(fact * ((RR(i:i + N - 1) - RR(i + 1:i + N))));
        sd2mod = std(fact * ((RR(i:i + N - 1) + RR(i + 1:i + N))));
        slope = abs(polyfit(tm(i:i + N), RR(i:i + N), 1));
        a(end+1) = slope(1);
        if sd1mod==0
            modCSI(i) = 0;
        else
           modCSI(i) = 4*sd2mod * sd2mod / sd1mod * slope(1);
        end
    end
    
    % calculation of csi
    CSI=ones(1,end_idx);
    for i = 1:end_idx
        sd1 = std(fact * ((RRbf(i:i + N - 1) - RRbf(i + 1:i + N))));
        sd2 = std(fact * ((RRbf(i:i + N - 1) + RRbf(i + 1:i + N))));
        if sd1mod==0
            CSI(i) = 0;
        else
           CSI(i) = sd2 / sd1 * a(i);
        end
    end   

  csi.CSI = CSI;
  csi.modCSI = modCSI;

  % estimated time axis
  time_start_idx=N/2;
  t = cumsum(rr)
  csi.t = t(time_start_idx+1:time_start_idx+length(CSI));

  % series for visualizing seizure start and end
  episode_times=[];
  for e=1:patient.valid_episodes
    episode_times = [episode_times patient.episodes(e,1) (patient.episodes(e,1)+patient.episodes(e,2))];
  end
  csi.episodes = zeros(1,length(CSI));
  mx = max(modCSI);
  for et=1:length(episode_times)
    idx=find(csi.t>episode_times(et)/params.sf);
    if length(idx)>0
        csi.episodes(idx(1))=mx;
    end
  end  
end
