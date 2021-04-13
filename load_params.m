function params = load_params()
    load s_b_coeff.mat;

    params.b_avg = b_avg;
    params.b_high = b_high;
    params.b_low = b_low;
    params.delay = delay;
    params.desc_filt = desc_filt;
    params.sf = 512;
    params.patient_info_path='patient_info';
    params.mat_file_path='mat';
    params.mat_filename_start='patient-all-';
    params.tdms_file_path = '**/*.tdms';
end
