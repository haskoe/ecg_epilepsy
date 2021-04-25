params = load_params();
start_idx=132000;
win_size = 20 * params.sf;
pid = '2';
patient = load_patient( pid, params);

samples = patient.samples(start_idx:start_idx+win_size-1);

%filters = ["sombrero" "butter_filter"];
f1 = make_sombrero(params.sf);
f2 = make_butter(1,0.6);
filters = {f1,f2};
compare_detect(samples,params,filters);

% i python
% compare_detect(samples,params,[make_sombrero() make_butter(order=2,cutoff_frac=0.6)]);
