import React from 'react';
import { useForm, Controller } from 'react-hook-form';
import { InputText } from 'primereact/inputtext';
import { classNames } from 'primereact/utils';
import { Button } from 'primereact/button';
import { defaultValue } from '../../shared/request.model';
import { createRequest } from '../../services/requests.service';
import '../../App.css';
import {useNavigate} from "react-router-dom";

export const NewRequest = () => {

  const defaultValues = {
    passenger: '',
    destination: ''
  }
  const { control, formState: { errors }, handleSubmit, reset } = useForm({ defaultValues });
  const navigate = useNavigate();

  const onSubmit = (data: any) => {
    console.log('DATA ', data);
    const newRequest = defaultValue;
    newRequest.passenger = data.passenger;
    newRequest.destination = data.destination;
    createRequest(newRequest).then(r => navigate(`/requests`));
    reset();
    //window.location.href = "/requests"
  };

  return (
    <>
      <div className="form-demo">
        <h2>Create a new Request</h2>
        <div>
          <div className='card'>
            <form onSubmit={handleSubmit(onSubmit)} className="p-fluid">
              <div className="field">
                <span className="p-float-label">
                  <Controller name="passenger" control={control} rules={{ required: 'Passenger is required.' }} render={({ field }) => (
                    <InputText id={field.name} {...field} />
                  )} />
                  <label htmlFor="passenger" className={classNames({ 'p-error': errors.passenger })}>Passenger*</label>
                </span>
              </div>
              <div className="field">
                <span className="p-float-label">
                  <Controller name="destination" control={control} rules={{ required: 'Destination is required.' }} render={({ field }) => (
                    <InputText id={field.name} {...field} />
                  )} />
                  <label htmlFor="destination" className={classNames({ 'p-error': errors.destination })}>Destination*</label>
                </span>
              </div>
              <Button type="submit" label="Save" />
            </form>
          </div>
        </div>
      </div>
    </>
  );
}

export default NewRequest;
