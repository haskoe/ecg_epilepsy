function patient = load_patient(patient_id,params)
    load( fullfile(params.mat_file_path, [params.mat_filename_start convertStringsToChars(patient_id)]));
    patient.num_episodes = size(patient.episodes,1);
    patient.num_samples = length(patient.samples);
    patient.valid_episodes = 0;
    for e=1:patient.num_episodes
        episode = patient.episodes(e,:);
        if episode(1)+episode(2)>patient.num_samples
            break
        end
        patient.valid_episodes = e;
    end
end    
