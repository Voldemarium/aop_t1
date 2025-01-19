CREATE TABLE data_source_error_log (
   id SERIAL,
   stack_trace VARCHAR,
   message VARCHAR,
   method_signature VARCHAR,
   PRIMARY KEY (id)
);