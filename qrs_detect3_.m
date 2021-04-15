function rr_indices = qrs_detect3_(samples,params)
    [rr_indices,sign,en_thres] = qrs_detect3(samples,0.25,0.6,params.sf); % refractory period changed from 0.25 -> 0.1 ms
end