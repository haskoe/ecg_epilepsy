function patient_pan_tomkin(pid, secs_before_episode, secs_after_episode)
    p = load_params();
    patient = load_patient(pid);
    for e=1:patient.valid_episodes
        sub_samples = get_episode_samples(patient,p.sf,e,secs_before_episode,secs_after_episode);

        [qrs_amp_raw,qrs_i_raw,delay]=pan_tompkin(sub_samples,p.sf,0);

        r_info = qrs_detect(sub_samples,p.sf,2);

        %qrs_detect_plot(sub_samples, secs_before_episode, r_info,'qrs_detect.m')

        plot_csi(r_info, 100, secs_before_episode);
    end
end
