package main

import (
	"github.com/api2pdf-pdf-generation-powered-by-aws-lambda/mcp-server/config"
	"github.com/api2pdf-pdf-generation-powered-by-aws-lambda/mcp-server/models"
	tools_merge_combine_pdfs "github.com/api2pdf-pdf-generation-powered-by-aws-lambda/mcp-server/tools/merge_combine_pdfs"
	tools_wkhtmltopdf "github.com/api2pdf-pdf-generation-powered-by-aws-lambda/mcp-server/tools/wkhtmltopdf"
	tools_headless_chrome "github.com/api2pdf-pdf-generation-powered-by-aws-lambda/mcp-server/tools/headless_chrome"
	tools_libreoffice "github.com/api2pdf-pdf-generation-powered-by-aws-lambda/mcp-server/tools/libreoffice"
)

func GetAll(cfg *config.APIConfig) []models.Tool {
	return []models.Tool{
		tools_merge_combine_pdfs.CreateMergepostTool(cfg),
		tools_wkhtmltopdf.CreateWkhtmltopdffromhtmlpostTool(cfg),
		tools_wkhtmltopdf.CreateWkhtmltopdffromurlgetTool(cfg),
		tools_wkhtmltopdf.CreateWkhtmltopdffromurlpostTool(cfg),
		tools_headless_chrome.CreateChromefromhtmlpostTool(cfg),
		tools_headless_chrome.CreateChromefromurlgetTool(cfg),
		tools_headless_chrome.CreateChromefromurlpostTool(cfg),
		tools_libreoffice.CreateLibreconvertpostTool(cfg),
	}
}
