params = load_params();
qrs_func_name = 'pan_tompkin_';
start_idx=20000;
win_size = 10 * params.sf;
pid = '2';
patient = load_patient( pid, params);

samples = patient.samples(start_idx:start_idx+win_size-1);
ex = feval( qrs_func_name, samples, params);

%sl = length(samples);
%x = 1:sl;
eb_height=0.1*(max(samples(1:1000))-min(samples(1:1000)));
ey=arrayfun(@(x) samples(x),ex);
err=eb_height*ones(length(ex),1);

hold on
plot(samples)
errorbar(ex,ey,err,'LineStyle','none');
hold off
