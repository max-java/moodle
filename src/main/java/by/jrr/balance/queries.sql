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
group by currency;

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
