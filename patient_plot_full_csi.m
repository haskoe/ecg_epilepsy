params = load_params();

pid = '5';
patient = load_patient(pid,params);
samples = patient.samples;
%samples = patient.samples(1:3*15*60*params.sf);

rr = calc_rr_qrs_detect3(samples,params);
csi = calc_csi(patient,params,rr,100);
plot(csi.t,csi.modCSI,csi.t,csi.CSI,csi.t,csi.episodes);
