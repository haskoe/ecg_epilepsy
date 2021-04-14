load s_b_coeff.mat; 
pid='4';
sf=512;

load( fullfile('mat', ['patient-all-' pid]));

num_periods=ecg(1);
%hold on
for p=1:num_periods
    %samples=ecg(2+num_periods*2:16541824); % :end);
    % window 10 minutes before and after first seizure
    samples_before=5*60*sf;
    %samples_before=5*sf;
    seizure_start_idx=ecg(2*p) + ecg(1)*2 + 2;
    seizure_length=ecg(2*p+1);

    samples=ecg(seizure_start_idx-samples_before:seizure_start_idx+seizure_length+samples_before);

    seizure = ecg(seizure_start_idx:seizure_start_idx+seizure_length);
    [qrs_amp_raw,qrs_i_raw,delay]=pan_tompkin(seizure,sf,0);
    %scatter(qrs_amp_raw(1:end-1),qrs_amp_raw(2:end))

    [qrs_amp_raw,qrs_i_raw,delay]=pan_tompkin(ecg(ecg(1)*2+2:samples_before),sf,0);
    %scatter(qrs_amp_raw(1:end-1),qrs_amp_raw(2:end))
    
    %samples=ecg(seizure_start_idx-samples_before:seizure_start_idx+samples_before);

    % virker ikke godt med patient 2
    [qrs_amp_raw,qrs_i_raw,delay]=pan_tompkin(seizure,sf,0);

    %[qrs_i_raw,varargout] = pantompkins_qrs(samples,sf);
    qrs=zeros(1,length(seizure));
    qrs=qrs(1,:);
    for i=1:length(qrs_i_raw)
        qrs(qrs_i_raw(i))=1;
    end
    t=1:length(seizure);
    %plot(t,samples,t,qrs*max(samples))
    plot(t,seizure,t,qrs*max(seizure),t,qrs*min(seizure))

    %rr=get_HRV(qrs, 512);

    [modCSI,CSI]=calc_csi(qrs_amp_raw,100);
    t=1:length(modCSI);
    plot(t,modCSI);
end
%hold off
