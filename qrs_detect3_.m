function r_info = qrs_detect3_(samples,params)
    [r_indices,sign,en_thres] = qrs_detect3(samples,0.25,0.6,params.sf); % refractory period changed from 0.25 -> 0.1 ms
    r_info.indices = r_indices;
    rr = diff(r_indices)/params.sf;
    r_info.rr = rr;
end