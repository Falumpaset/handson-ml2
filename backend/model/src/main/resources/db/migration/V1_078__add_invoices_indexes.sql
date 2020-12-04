CREATE INDEX IF NOT EXISTS idx_invoice_date ON landlord.invoice(invoice_date);
CREATE INDEX IF NOT EXISTS idx_invoice_date_cancelled ON landlord.invoice(invoice_date, cancellation);
