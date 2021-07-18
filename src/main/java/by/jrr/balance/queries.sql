# filter operations by quarter
select * from operation_row where QUARTER(date) = 1 AND YEAR(date) = '2021';

# filter operations by quarter with categories names
select date, operation_row.operation_row_direction, sum, currency, name, note from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1 AND YEAR(date) = '2021'
and operation_row_direction = 'OUTCOME'
;

select date, operation_row.operation_row_direction, sum, currency, name, note from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1 AND YEAR(date) = '2021'
and operation_row_direction = 'INCOME'
;

# calculate sums for quarter
select SUM(sum), currency, any_value(operation_row_direction)
from operation_row
where QUARTER(date) = 1 AND YEAR(date) = '2021' and operation_row_direction = 'OUTCOME'
group by currency
union
select SUM(sum), currency, any_value(operation_row_direction)
from operation_row
where QUARTER(date) = 1 AND YEAR(date) = '2021' and operation_row_direction = 'INCOME'
group by currency;

# calculate sum for quarter for selected notes
select SUM(sum), currency, any_value(operation_row_direction)
from operation_row
where QUARTER(date) = 1 AND YEAR(date) = '2021' and operation_row_direction = 'OUTCOME'
  and note like '%%'
group by currency;

select SUM(sum), currency, name, any_value(operation_row.operation_row_direction)
from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1 AND YEAR(date) = '2021' and operation_row.operation_row_direction = 'OUTCOME'
group by name, currency;



# totals for trainers and sale managers
select SUM(sum), currency, name, any_value(operation_row.operation_row_direction)
from operation_row
         left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1 AND YEAR(date) = '2021' and operation_row.operation_row_direction = 'OUTCOME'
group by name, currency
;

# totals for income fact
select count(*) as total, SUM(sum) as Sum, currency, name, any_value(operation_row.operation_row_direction)
from operation_row
         left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1 AND YEAR(date) = '2021' and operation_row.operation_row_direction = 'INCOME'
group by name, currency;

# INVOICES per quarter for income fact
select count(*) as total, SUM(sum) as Sum, currency, name, any_value(operation_row.operation_row_direction)
from operation_row
         left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1 AND YEAR(date) = '2021' and operation_row.operation_row_direction = 'INVOICE'
group by name, currency;

# new contracts in quarter
select count(*) as total, SUM(sum) as Sum, currency, name, any_value(operation_row.operation_row_direction)
from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1 AND YEAR(date) = '2021' and operation_row.operation_row_direction = 'CONTRACT'
group by name, currency;

# ALL ABOVE
select count(*) as total, SUM(sum) as Sum, currency, name, operation_row.operation_row_direction
from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1 AND YEAR(date) = '2021'
group by name, currency, operation_row.operation_row_direction;

# non categorized outcomes
select date, operation_row.operation_row_direction, sum, currency, note from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME';

# filters for non categorized outcomes
select SUM(sum) as Sum, currency, substr(note, position('fb' in note), length('fb')) as notes from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME'
  and note like '%fb%'
group by currency, notes;

# Common spending united
select SUM(sum) as Sum, currency, substr(note, position('fb' in note), length('fb')) as notes from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME'
  and note like '%fb%'
group by currency, notes
union
select SUM(sum) as Sum, currency, substr(note, position('zoom' in note), length('zoom')) as notes from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME'
  and note like '%zoom%'
group by currency, notes
union
select SUM(sum) as Sum, currency, substr(note, position('bezkassira' in note), length('bezkassira')) as notes from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME'
  and note like '%bezkassira%'
group by currency, notes
union
select SUM(sum) as Sum, currency, substr(note, position('АРЕНДНАЯ' in note), 8) as notes from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME'
  and note like '%АРЕНДНАЯ%'
group by currency, notes
union
select SUM(sum) as Sum, currency, substr(note, position('SMM ПРОДВИЖЕНИЕ' in note), 15) as notes from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME'
  and note like '%SMM ПРОДВИЖЕНИЕ%'
group by currency, notes
union
select SUM(sum) as Sum, currency, substr(note, position('google' in note), length('google')) as notes from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME'
  and note like '%google%'
group by currency, notes
union
select SUM(sum) as Sum, currency, substr(note, position('Налог' in note), 6) as notes from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME'
  and note like '%Налог%'
group by currency, notes
union
select SUM(sum) as Sum, currency, substr(note, position('a1' in note), length('a1')) as notes from operation_row
left join operation_category on operation_row.id_operation_category=operation_category.id
where QUARTER(date) = 1
  AND YEAR(date) = '2021'
  and name is null
  and note not like 'перенос%'
  and operation_row.operation_row_direction = 'OUTCOME'
  and note like '%a1%'
group by currency, notes;

# contracts
select
    contract.date,
    contract_type.name as contract_type,
    operation_row.currency,
    operation_row.sum,
    student_user.first_and_last_name as student,
    stream_user.name as stream
from contract
         join contract_type on contract.contract_type_id=contract_type.id
         join contract_to_operation_row on contract_to_operation_row.contract_id=contract.id
         join operation_row on operation_row.id=contract_to_operation_row.operation_row_id
         join contract_to_profile on contract_to_profile.contract_id=contract.id
         join profile as stream on stream.id=contract_to_profile.stream_id
         join profile as student on student.id=contract_to_profile.subscriber_id
         join users as stream_user on stream_user.user_id=stream.user_id
         join users as student_user on student_user.user_id=student.user_id
where QUARTER(contract.date) = 1 AND YEAR(contract.date) = '2021';

# contracts by types with count
select
    count(*) as total,
    contract_type.name as contract_type
from contract
         join contract_type on contract.contract_type_id=contract_type.id
         join contract_to_operation_row on contract_to_operation_row.contract_id=contract.id
         join operation_row on operation_row.id=contract_to_operation_row.operation_row_id
         join contract_to_profile on contract_to_profile.contract_id=contract.id
         join profile as stream on stream.id=contract_to_profile.stream_id
         join profile as student on student.id=contract_to_profile.subscriber_id
         join users as stream_user on stream_user.user_id=stream.user_id
         join users as student_user on student_user.user_id=student.user_id
where QUARTER(contract.date) = 1 AND YEAR(contract.date) = '2021'
group by contract_type
with rollup;

# contracts by streams
select
    count(*) as total,
    stream_user.name
from contract
         join contract_type on contract.contract_type_id=contract_type.id
         join contract_to_operation_row on contract_to_operation_row.contract_id=contract.id
         join operation_row on operation_row.id=contract_to_operation_row.operation_row_id
         join contract_to_profile on contract_to_profile.contract_id=contract.id
         join profile as stream on stream.id=contract_to_profile.stream_id
         join profile as student on student.id=contract_to_profile.subscriber_id
         join users as stream_user on stream_user.user_id=stream.user_id
         join users as student_user on student_user.user_id=student.user_id
where QUARTER(contract.date) = 1 AND YEAR(contract.date) = '2021'
group by stream_user.name
with rollup;

# contracts by streams and types
select
    count(*) as total,
    stream_user.name as stream_name,
    contract_type.name as contract_type,
    sum(operation_row.sum),
    operation_row.currency
from contract
         join contract_type on contract.contract_type_id=contract_type.id
         join contract_to_operation_row on contract_to_operation_row.contract_id=contract.id
         join operation_row on operation_row.id=contract_to_operation_row.operation_row_id
         join contract_to_profile on contract_to_profile.contract_id=contract.id
         join profile as stream on stream.id=contract_to_profile.stream_id
         join profile as student on student.id=contract_to_profile.subscriber_id
         join users as stream_user on stream_user.user_id=stream.user_id
         join users as student_user on student_user.user_id=student.user_id
where QUARTER(contract.date) = 1 AND YEAR(contract.date) = '2021'
group by stream_user.name, contract_type, operation_row.currency
order by stream_name;

# incomes by streams
select SUM(sum), currency, any_value(operation_row_direction), users.name
from operation_row
         join operation_to_profile otp on operation_row.id = otp.operation_row_id
         join profile on otp.stream_id = profile.id
         join users on profile.user_id = users.user_id
where MONTH(date) = 6 AND YEAR(date) = '2021' and operation_row_direction = 'INCOME'
group by currency, name;

#incoms Victor groups
select SUM(sum), currency, any_value(operation_row_direction), name
from operation_row
         join operation_to_profile otp on operation_row.id = otp.operation_row_id
         join profile on otp.stream_id = profile.id
         join users on profile.user_id = users.user_id
where YEAR(date) = '2021' and operation_row_direction = 'INCOME'
  AND (name = 'Java 1: Core' or name = 'Java EE web' or name like '%javaAz%')
group by currency, name
with rollup;

#outcoms by trainers
select SUM(sum), currency, name, last_name
from operation_row
         join operation_to_profile otp on operation_row.id = otp.operation_row_id
         join profile on otp.subscriber_id = profile.id
         join users on profile.user_id = users.user_id
where YEAR(date) = '2021' and operation_row_direction = 'OUTCOME'
group by currency, name, last_name
with rollup;
