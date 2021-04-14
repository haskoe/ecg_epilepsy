% plot af csi på det fulde signal hvor signalet er splittet op pga formodet
% memory overflow i QRS detect funktionen
params = load_params();

qrs_win_size = 30*60*params.sf; % 30 min. window size
csi_win_size=100; % CSI window size
skip_start=5;
pid = '5';
patient = load_patient(pid,params);

% uncomment line below to perform QRS detection on the full signal
% Tested: doesnt work
% qrs_win_size = length(patient.samples);

num_win=ceil(length(patient.samples) / qrs_win_size);
mod_csi_all = [];
csi_all= [];
for w=1:num_win
    start_idx = qrs_win_size*(w-1);
    end_idx=start_idx+qrs_win_size;
    if end_idx>length(patient.samples)
        end_idx=length(patient.samples);
    end
    sub_samples = patient.samples(1+start_idx:end_idx);
    r_info = qrs_detect3_(sub_samples,params);
    length(r_info.rr);
    [modCSI,CSI]=calc_csi(r_info.rr,csi_win_size);
    %HRV=pan_tompkin(sub_samples,params.sf,0);
    %[modCSI,CSI]=calc_csi(HRV,csi_win_size);
    mod_csi_all = [mod_csi_all modCSI(skip_start+1:end)];
    csi_all = [csi_all CSI(skip_start+1:end)];
end    
hold on
plot(mod_csi_all);
plot(csi_all);
hold off
