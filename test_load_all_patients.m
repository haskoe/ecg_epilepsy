function patient = test_load_all_patients()
    pids=get_all_patient_ids();
    for p=1:length(pids)
        patient = load_patient(pids(p));
    end
end    
