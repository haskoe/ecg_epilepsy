function rr = calc_rr(qrs_func_name,samples, params)
    secs_win_size =  15 * 60; % 30 min. window size5
    sample_win_size = secs_win_size * params.sf; % 30 min. window size5
    num_windows = ceil(length(samples) / sample_win_size);
    RR_MIN = 0.1;
    RR_MAX = 3;

    rr = [];
    l = length(samples);
    for w=1:num_windows
        start_idx = sample_win_size*(w-1);
        end_idx=start_idx+sample_win_size;
        if end_idx>l
            end_idx=l;
        end
        rr_indices = feval( qrs_func_name, samples(start_idx+1:end_idx), params);
        
        % calculate RR
        temp = diff([1 rr_indices]) / params.sf;
        
        % remove RR values below RR_MIN and larger than RR_MAX
        temp = temp((temp>=RR_MIN) & (temp<=RR_MAX));
        
        % append artificial RR values so accumulated time is sufficiently
        % close to be sample_win_size secs
        acc_time = sum(temp);
        
        if acc_time<secs_win_size-temp(1)
            % calculate extra RR elements needed
            rr_extra = round((secs_win_size-acc_time)/temp(1));
            temp = [ones(1,rr_extra)*temp(1) temp];
        end
        
        % acc_time check 
        acc_time = sum(temp);
        rr = [rr temp];
    end
end