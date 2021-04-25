function rr = calc_rr_no_fill(qrs_detect_func,samples,params)
    secs_win_size =  15 * 60; % 15 min. window size5
    sample_win_size = secs_win_size * params.sf; % 30 min. window size5
    num_windows = ceil(length(samples) / sample_win_size);

    rr = [];
    num_samples = length(samples);
    for w=1:num_windows
        start_idx = sample_win_size*(w-1);
        end_idx=start_idx+sample_win_size;
        if end_idx>num_samples
            end_idx=num_samples;
            secs_win_size = (end_idx-start_idx) / params.sf;
        end
        rr_indices = qrs_detect_func(samples(start_idx+1:end_idx));
        
        rr = [rr diff([rr_indices]) / params.sf];
    end
end