function patient = load_patient(patient_id,params)
    load( fullfile(params.mat_file_path, [params.mat_filename_start patient_id]));
    skip_samples = 30*60*params.sf;
    patient.samples=patient.samples(skip_samples+1:length(patient.samples)-skip_samples);
    patient.num_episodes = size(patient.episodes,1);
    patient.num_samples = length(patient.samples);
    episodes=[];
    patient.valid_episodes = 0;
    for e=1:patient.num_episodes
        patient.episodes(e,1) = patient.episodes(e,1) - skip_samples;
        episode = patient.episodes(e,:);
        if episode(1)>=0
            if episode(1)+episode(2)>patient.num_samples
                break
            end
            episodes(end+1,:)=episode;
            patient.valid_episodes = patient.valid_episodes+1;
        end
    end
    patient.episodes = episodes;
    patient.num_episodes = size(patient.episodes,1);
end    
