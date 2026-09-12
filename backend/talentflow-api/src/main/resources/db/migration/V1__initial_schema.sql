-- Active: 1788546929017@@localhost@3306@mysql
-- TalentFlow V1 Initial Schema
-- Source of truth for the initial relational schema.
-- Apply through Flyway only. Do not execute manually against shared environments.

CREATE TABLE `user` (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    role VARCHAR(30) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uk_user_username UNIQUE (username),
    CONSTRAINT uk_user_email UNIQUE (email),
    CONSTRAINT chk_user_role CHECK (role IN ('ADMIN', 'REVIEWER', 'VIEWER'))
);

CREATE INDEX idx_user_role ON `user` (role);
CREATE INDEX idx_user_is_active ON `user` (is_active);


CREATE TABLE client (
    id BIGINT NOT NULL AUTO_INCREMENT,
    client_code VARCHAR(50) NOT NULL,
    client_name VARCHAR(255) NOT NULL,
    industry VARCHAR(100) NULL,
    contact_name VARCHAR(150) NULL,
    contact_email VARCHAR(255) NULL,
    contact_phone VARCHAR(30) NULL,
    notes TEXT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_client PRIMARY KEY (id),
    CONSTRAINT uk_client_code UNIQUE (client_code)
);

CREATE INDEX idx_client_name ON client (client_name);
CREATE INDEX idx_client_is_active ON client (is_active);


CREATE TABLE requirement (
    id BIGINT NOT NULL AUTO_INCREMENT,
    rr_number VARCHAR(50) NOT NULL,
    client_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    employment_type VARCHAR(50) NULL,
    location VARCHAR(200) NULL,
    work_mode VARCHAR(30) NOT NULL,
    onshore_or_offshore VARCHAR(20) NOT NULL,
    min_experience_years DECIMAL(4,1) NULL,
    max_experience_years DECIMAL(4,1) NULL,
    positions_count INT NOT NULL,
    priority VARCHAR(20) NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    created_by BIGINT NOT NULL,
    opened_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    target_fill_date DATE NULL,
    closed_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_requirement PRIMARY KEY (id),
    CONSTRAINT uk_requirement_rr_number UNIQUE (rr_number),
    CONSTRAINT fk_requirement_client
        FOREIGN KEY (client_id) REFERENCES client (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_requirement_created_by
        FOREIGN KEY (created_by) REFERENCES `user` (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_requirement_work_mode
        CHECK (work_mode IN ('ONSITE', 'HYBRID', 'REMOTE')),
    CONSTRAINT chk_requirement_onshore_offshore
        CHECK (onshore_or_offshore IN ('ONSHORE', 'OFFSHORE')),
    CONSTRAINT chk_requirement_experience
        CHECK (
            (min_experience_years IS NULL OR min_experience_years >= 0)
            AND
            (max_experience_years IS NULL OR max_experience_years >= 0)
            AND
            (min_experience_years IS NULL OR max_experience_years IS NULL
             OR min_experience_years <= max_experience_years)
        ),
    CONSTRAINT chk_requirement_positions
        CHECK (positions_count > 0),
    CONSTRAINT chk_requirement_priority
        CHECK (priority IS NULL OR priority IN ('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')),
    CONSTRAINT chk_requirement_status
        CHECK (status IN ('OPEN', 'FILLED', 'CLOSED'))
);

CREATE INDEX idx_requirement_client_id ON requirement (client_id);
CREATE INDEX idx_requirement_status ON requirement (status);
CREATE INDEX idx_requirement_created_at ON requirement (created_at);
CREATE INDEX idx_requirement_target_fill_date ON requirement (target_fill_date);


CREATE TABLE skill (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    normalized_name VARCHAR(100) NOT NULL,
    category VARCHAR(100) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_skill PRIMARY KEY (id),
    CONSTRAINT uk_skill_normalized_name UNIQUE (normalized_name)
);


CREATE TABLE requirement_skill (
    requirement_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    importance VARCHAR(20) NOT NULL,
    minimum_years DECIMAL(4,1) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_requirement_skill PRIMARY KEY (requirement_id, skill_id),
    CONSTRAINT fk_requirement_skill_requirement
        FOREIGN KEY (requirement_id) REFERENCES requirement (id)
        ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT fk_requirement_skill_skill
        FOREIGN KEY (skill_id) REFERENCES skill (id)
        ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT chk_requirement_skill_importance
        CHECK (importance IN ('REQUIRED', 'PREFERRED')),
    CONSTRAINT chk_requirement_skill_minimum_years
        CHECK (minimum_years IS NULL OR minimum_years >= 0)
);


CREATE TABLE candidate (
    id BIGINT NOT NULL AUTO_INCREMENT,
    candidate_reference VARCHAR(50) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(30) NULL,
    total_experience_years DECIMAL(4,1) NULL,
    current_title VARCHAR(200) NULL,
    current_company VARCHAR(255) NULL,
    current_location VARCHAR(200) NULL,
    preferred_location VARCHAR(200) NULL,
    notice_period_days INT NULL,
    profile_summary TEXT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_by BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_candidate PRIMARY KEY (id),
    CONSTRAINT uk_candidate_reference UNIQUE (candidate_reference),
    CONSTRAINT fk_candidate_created_by
        FOREIGN KEY (created_by) REFERENCES `user` (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_candidate_experience
        CHECK (total_experience_years IS NULL OR total_experience_years >= 0),
    CONSTRAINT chk_candidate_notice_period
        CHECK (notice_period_days IS NULL OR notice_period_days >= 0),
    CONSTRAINT chk_candidate_status
        CHECK (status IN ('ACTIVE', 'INACTIVE', 'WITHDRAWN'))
);

CREATE INDEX idx_candidate_email ON candidate (email);
CREATE INDEX idx_candidate_status ON candidate (status);
CREATE INDEX idx_candidate_created_at ON candidate (created_at);


CREATE TABLE resume_document (
    id BIGINT NOT NULL AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    original_file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL,
    storage_path VARCHAR(1000) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    file_hash VARCHAR(128) NULL,
    extracted_text LONGTEXT NULL,
    parsing_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    uploaded_by BIGINT NOT NULL,
    uploaded_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_resume_document PRIMARY KEY (id),
    CONSTRAINT fk_resume_candidate
        FOREIGN KEY (candidate_id) REFERENCES candidate (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_resume_uploaded_by
        FOREIGN KEY (uploaded_by) REFERENCES `user` (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_resume_file_size
        CHECK (file_size_bytes > 0),
    CONSTRAINT chk_resume_parsing_status
        CHECK (parsing_status IN ('PENDING', 'COMPLETED', 'FAILED'))
);

CREATE INDEX idx_resume_candidate_id ON resume_document (candidate_id);
CREATE INDEX idx_resume_parsing_status ON resume_document (parsing_status);


CREATE TABLE candidate_skill (
    candidate_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    proficiency VARCHAR(30) NULL,
    years_experience DECIMAL(4,1) NULL,
    source VARCHAR(30) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_candidate_skill PRIMARY KEY (candidate_id, skill_id),
    CONSTRAINT fk_candidate_skill_candidate
        FOREIGN KEY (candidate_id) REFERENCES candidate (id)
        ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT fk_candidate_skill_skill
        FOREIGN KEY (skill_id) REFERENCES skill (id)
        ON DELETE CASCADE ON UPDATE RESTRICT,
    CONSTRAINT chk_candidate_skill_years
        CHECK (years_experience IS NULL OR years_experience >= 0),
    CONSTRAINT chk_candidate_skill_source
        CHECK (source IN ('MANUAL', 'RESUME', 'AI'))
);


CREATE TABLE candidate_submission (
    id BIGINT NOT NULL AUTO_INCREMENT,
    candidate_id BIGINT NOT NULL,
    requirement_id BIGINT NOT NULL,
    current_status VARCHAR(30) NOT NULL DEFAULT 'PROFILE_RECEIVED',
    submitted_by BIGINT NOT NULL,
    assigned_to BIGINT NULL,
    submission_notes TEXT NULL,
    assigned_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_candidate_submission PRIMARY KEY (id),
    CONSTRAINT uk_candidate_requirement UNIQUE (candidate_id, requirement_id),
    CONSTRAINT fk_submission_candidate
        FOREIGN KEY (candidate_id) REFERENCES candidate (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_submission_requirement
        FOREIGN KEY (requirement_id) REFERENCES requirement (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_submission_submitted_by
        FOREIGN KEY (submitted_by) REFERENCES `user` (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_submission_assigned_to
        FOREIGN KEY (assigned_to) REFERENCES `user` (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_submission_status
        CHECK (current_status IN (
            'PROFILE_RECEIVED',
            'L1_PENDING',
            'L1_SCHEDULED',
            'L1_CLEARED',
            'L1_REJECTED',
            'CI_PENDING',
            'CI_SCHEDULED',
            'CI_ACCEPTED',
            'CI_REJECTED',
            'WITHDRAWN'
        ))
);

CREATE INDEX idx_submission_candidate_id ON candidate_submission (candidate_id);
CREATE INDEX idx_submission_requirement_id ON candidate_submission (requirement_id);
CREATE INDEX idx_submission_current_status ON candidate_submission (current_status);
CREATE INDEX idx_submission_assigned_to ON candidate_submission (assigned_to);
CREATE INDEX idx_submission_created_at ON candidate_submission (created_at);


CREATE TABLE l1_evaluation (
    id BIGINT NOT NULL AUTO_INCREMENT,
    submission_id BIGINT NOT NULL,
    interviewer_id BIGINT NOT NULL,
    scheduled_at DATETIME NULL,
    duration_minutes INT NULL,
    meeting_link VARCHAR(1000) NULL,
    conducted_at DATETIME NULL,
    result VARCHAR(30) NULL,
    technical_comments TEXT NULL,
    overall_comments TEXT NULL,
    recommendation VARCHAR(30) NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_l1_evaluation PRIMARY KEY (id),
    CONSTRAINT fk_l1_submission
        FOREIGN KEY (submission_id) REFERENCES candidate_submission (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_l1_interviewer
        FOREIGN KEY (interviewer_id) REFERENCES `user` (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_l1_duration
        CHECK (duration_minutes IS NULL OR duration_minutes > 0),
    CONSTRAINT chk_l1_result
        CHECK (result IS NULL OR result IN ('CLEARED', 'REJECTED'))
);

CREATE INDEX idx_l1_submission_id ON l1_evaluation (submission_id);
CREATE INDEX idx_l1_interviewer_id ON l1_evaluation (interviewer_id);
CREATE INDEX idx_l1_scheduled_at ON l1_evaluation (scheduled_at);
CREATE INDEX idx_l1_result ON l1_evaluation (result);


CREATE TABLE client_interview (
    id BIGINT NOT NULL AUTO_INCREMENT,
    submission_id BIGINT NOT NULL,
    client_interviewer_name VARCHAR(150) NULL,
    client_interviewer_email VARCHAR(255) NULL,
    scheduled_at DATETIME NULL,
    duration_minutes INT NULL,
    meeting_link VARCHAR(1000) NULL,
    conducted_at DATETIME NULL,
    result VARCHAR(30) NULL,
    comments TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_client_interview PRIMARY KEY (id),
    CONSTRAINT fk_client_interview_submission
        FOREIGN KEY (submission_id) REFERENCES candidate_submission (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_client_interview_duration
        CHECK (duration_minutes IS NULL OR duration_minutes > 0),
    CONSTRAINT chk_client_interview_result
        CHECK (result IS NULL OR result IN ('ACCEPTED', 'REJECTED'))
);

CREATE INDEX idx_client_interview_submission_id ON client_interview (submission_id);
CREATE INDEX idx_client_interview_scheduled_at ON client_interview (scheduled_at);
CREATE INDEX idx_client_interview_result ON client_interview (result);


CREATE TABLE status_history (
    id BIGINT NOT NULL AUTO_INCREMENT,
    submission_id BIGINT NOT NULL,
    old_status VARCHAR(30) NULL,
    new_status VARCHAR(30) NOT NULL,
    changed_by BIGINT NOT NULL,
    transition_reason VARCHAR(255) NULL,
    comments TEXT NULL,
    changed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_status_history PRIMARY KEY (id),
    CONSTRAINT fk_status_history_submission
        FOREIGN KEY (submission_id) REFERENCES candidate_submission (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_status_history_changed_by
        FOREIGN KEY (changed_by) REFERENCES `user` (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_status_history_new_status
        CHECK (new_status IN (
            'PROFILE_RECEIVED',
            'L1_PENDING',
            'L1_SCHEDULED',
            'L1_CLEARED',
            'L1_REJECTED',
            'CI_PENDING',
            'CI_SCHEDULED',
            'CI_ACCEPTED',
            'CI_REJECTED',
            'WITHDRAWN'
        ))
);

CREATE INDEX idx_status_history_submission_changed_at
    ON status_history (submission_id, changed_at);
CREATE INDEX idx_status_history_changed_by
    ON status_history (changed_by);


CREATE TABLE ai_assessment (
    id BIGINT NOT NULL AUTO_INCREMENT,
    submission_id BIGINT NOT NULL,
    resume_document_id BIGINT NULL,
    model_name VARCHAR(100) NULL,
    prompt_version VARCHAR(30) NULL,
    assessment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    matched_skills JSON NULL,
    missing_skills JSON NULL,
    evidence JSON NULL,
    interview_questions JSON NULL,
    summary TEXT NULL,
    raw_response JSON NULL,
    error_message TEXT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME NULL,

    CONSTRAINT pk_ai_assessment PRIMARY KEY (id),
    CONSTRAINT fk_ai_assessment_submission
        FOREIGN KEY (submission_id) REFERENCES candidate_submission (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_ai_assessment_resume
        FOREIGN KEY (resume_document_id) REFERENCES resume_document (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT chk_ai_assessment_status
        CHECK (assessment_status IN ('PENDING', 'COMPLETED', 'FAILED'))
);

CREATE INDEX idx_ai_assessment_submission_id ON ai_assessment (submission_id);
CREATE INDEX idx_ai_assessment_status ON ai_assessment (assessment_status);
CREATE INDEX idx_ai_assessment_created_at ON ai_assessment (created_at);


CREATE TABLE notification (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    message VARCHAR(1000) NOT NULL,
    reference_type VARCHAR(50) NULL,
    reference_id BIGINT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at DATETIME NULL,

    CONSTRAINT pk_notification PRIMARY KEY (id),
    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id) REFERENCES `user` (id)
        ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE INDEX idx_notification_user_read ON notification (user_id, is_read);
CREATE INDEX idx_notification_created_at ON notification (created_at);
