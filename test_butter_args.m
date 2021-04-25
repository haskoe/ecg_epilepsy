params = load_params();
start_idx=132000;
win_size = 200 * params.sf;
pid = '2';
patient = load_patient( pid, params);

samples = patient.samples(start_idx:start_idx+win_size-1);

qrs_detect_funcs = {};
for o=1:6
    qrs_detect_funcs{end+1} = make_qrs_detect3(make_butter(o,0.01), params);
end
fracts = 0.5:0.05:0.8;
fracts = 0.01:0.02:0.1;
%for f=1:length(fracts)
%    qrs_detect_funcs{end+1} = make_qrs_detect3(make_butter(order,fracts(f)), params);
%end
qrs_detect_funcs{end+1} = make_qrs_detect3(make_sombrero(params.sf), params);

binranges=0.3:0.1:1;
qrs_detect_histogram(samples,params,qrs_detect_funcs,binranges)