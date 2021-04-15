function rr = calc_rr(qrs_func_name,samples, params)
    qrs_win_size = 30 * 60 * params.sf; % 30 min. window size
    num_windows = ceil(length(samples) / qrs_win_size);

    rr_acc = [];
    l = length(samples);
    for w=1:num_windows
        start_idx = qrs_win_size*(w-1);
        end_idx=start_idx+qrs_win_size;
        if end_idx>l
            end_idx=l;
        end
        rr_indices = feval( qrs_func_name, samples(start_idx+1:end_idx), params);
        if length(rr_acc)<=0
            rr_acc = rr_indices;
        else
            % append to already calculated rr indices with an additional rr interval 
            % of length = rr(end) inserted just before the new indices
            rr_acc = [rr_acc rr_acc(end) rr_indices];
        end
    end

    rr.indices = rr_acc;
    rr.rr = diff(rr_acc)/params.sf;
end