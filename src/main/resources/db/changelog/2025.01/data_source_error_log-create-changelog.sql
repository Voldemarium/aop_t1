CREATE TABLE data_source_error_log (
   id BIGSERIAL,
   stack_trace VARCHAR,
   message VARCHAR,
   method_signature VARCHAR,
   PRIMARY KEY (id)
);