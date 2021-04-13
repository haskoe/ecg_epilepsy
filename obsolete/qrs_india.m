function [rr,r_indices] = qrs_india(samples,sf)
    [r_indices,sign,en_thres] = qrs_detect3(samples,0.1,0.6,sf); % refractory period changed from 0.25 -> 0.1 ms
    rr = diff(r_indices)/sf;
end