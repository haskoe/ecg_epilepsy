params = load_params();

pid = '16';
patient = load_patient( pid, params);

ls=length(patient.samples);
start_idx=ceil(9.2/200*ls);
win_size = ceil(9.4/200*ls) - start_idx;

samples = patient.samples(start_idx:start_idx+win_size-1);

%filters = ["sombrero" "butter_filter"];
f1 = make_sombrero(params.sf);
f2 = make_butter(1,0.15);
filters = {f1,f2};
compare_detect(samples,params,filters);

% i python
% compare_detect(samples,params,[make_sombrero() make_butter(order=2,cutoff_frac=0.6)]);
