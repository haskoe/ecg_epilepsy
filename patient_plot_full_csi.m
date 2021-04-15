params = load_params();

pid = '2';
patient = load_patient(pid,params);
samples = patient.samples(1:3*30 * 60 * params.sf);

rr = calc_rr_qrs_detect3(samples,params)
csi = calc_csi(patient,rr,100);
hold on
plot(csi.modCSI);
plot(csi.csi);
hold off
