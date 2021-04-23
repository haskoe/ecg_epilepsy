params = load_params();

pid = '16';
patient = load_patient(pid,params);
episode = patient.episodes(1,:);
seizure_samples = patient.samples(episode(1,1)+1:episode(1,1)+episode(1,2));
plot(seizure_samples);

annotations=1:400:20000;

hold on
plot(1:length(seizure_samples),seizure_samples)
bar(annotations,ones(length(annotations),1)*max(seizure_samples),'BarWidth', 0.02);
hold off


