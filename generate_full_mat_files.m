files = dir('**/*.tdms');
sf = 512;

for i = 1:length(files)
    splt = split(files(i).name(9:end), '_');
    pid = splt{1};
    csv_fname = fullfile('patients', ['patient-ofs-' pid '.csv']);

    if isfile(csv_fname)
        ofs = readtable(csv_fname);

        % first element is number of seizures
        info = [];

        % load patient TDMS file
        Output = TDMS_getStruct(fullfile(files(i).folder, files(i).name));
        %Output.Untitled.EKG.Props.wf_start_time
        csv_fname
        %ofs(size(ofs,1),1).start*512
        l = length(Output.Untitled.EKG.data(1, :));

        for s = 1:size(ofs, 1)
            seizure_start_idx = ofs(s, 1).start * sf;
            seizure_samples = ofs(s, 2).length * sf;

            if seizure_start_idx + seizure_samples <= l
                info = [info seizure_start_idx seizure_samples];
            else
                s
            end

        end

        if length(info) > 1
            length(Output.Untitled.EKG.data(1, :))
            patient.id = pid;
            patient.num_episodes = length(info) / 2;
            patient.episodes = info;
            patient.samples = Output.Untitled.EKG.data(1, :);
            %ecg = [(length(info) / 2) info Output.Untitled.EKG.data(1, :)];
            fname = fullfile('mat', ['patient-all-' pid]);
            save(ecg_fname, 'patient')
        end

    end

end
