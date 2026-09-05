| Current State    | Action        | Next State   |
| ---------------- | ------------- | ------------ |
| PROFILE_RECEIVED | Submit for L1 | L1_PENDING   |
| L1_PENDING       | Schedule L1   | L1_SCHEDULED |
| L1_SCHEDULED     | Clear L1      | L1_CLEARED   |
| L1_SCHEDULED     | Reject L1     | L1_REJECTED  |
| L1_CLEARED       | Move to CI    | CI_PENDING   |
| CI_PENDING       | Schedule CI   | CI_SCHEDULED |
| CI_SCHEDULED     | Accept        | CI_ACCEPTED  |
| CI_SCHEDULED     | Reject        | CI_REJECTED  |
