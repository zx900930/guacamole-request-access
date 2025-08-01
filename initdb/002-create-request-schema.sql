--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements.  See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership.  The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License.  You may obtain a copy of the License at
--
--   http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied.  See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

--
-- Create tables for request and reservation system
--

CREATE TABLE guacamole_request_environment (

  environment_id    SERIAL       NOT NULL,
  environment_name  VARCHAR(128) NOT NULL,
  
  PRIMARY KEY (environment_id)

);

CREATE TABLE guacamole_request (

  request_id          SERIAL       NOT NULL,
  environment_id      INTEGER      NOT NULL,
  user_id             INTEGER      NOT NULL,
  reason              TEXT         NOT NULL,
  estimated_time      INTEGER      NOT NULL, -- in minutes
  submission_time     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  request_status      VARCHAR(16)  NOT NULL DEFAULT 'PENDING', -- PENDING, APPROVED, DENIED

  PRIMARY KEY (request_id),

  CONSTRAINT guacamole_request_ibfk_1
    FOREIGN KEY (environment_id)
    REFERENCES guacamole_request_environment (environment_id)
    ON DELETE CASCADE,

  CONSTRAINT guacamole_request_ibfk_2
    FOREIGN KEY (user_id)
    REFERENCES guacamole_user (user_id)
    ON DELETE CASCADE

);
